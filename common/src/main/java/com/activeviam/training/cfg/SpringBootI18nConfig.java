package com.activeviam.training.cfg;

import com.qfs.content.service.IContentEntry;
import com.qfs.content.service.IContentService;
import com.qfs.content.service.impl.PrefixedContentService;
import com.qfs.fwk.services.ServiceException;
import com.qfs.pivot.builder.discovery.impl.CubeFormatterFactory;
import com.qfs.pivot.content.IActivePivotContentService;
import com.qfs.server.cfg.content.IActivePivotContentServiceConfig;
import com.qfs.server.cfg.i18n.impl.I18nConfig;
import com.qfs.util.impl.QfsFiles;
import com.quartetfs.biz.pivot.cube.formatter.ICubeFormatterFactory;
import com.quartetfs.biz.pivot.security.builders.ICanHaveRoleEntitlements;
import com.quartetfs.fwk.QuartetRuntimeException;
import com.quartetfs.fwk.Registry;
import com.quartetfs.fwk.types.IPlugin;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import static com.activeviam.training.cfg.security.SecurityConfig.ROLE_ADMIN;
import static com.qfs.content.service.IContentService.ROLE_ROOT_LIST;

public class SpringBootI18nConfig extends I18nConfig {

    private static Logger LOGGER = Logger.getLogger(SpringBootI18nConfig.class.getName());
    protected static final String I18N_FOLDER = "i18n";
    public static final String I18N_RESOURCE_FOLDER = "i18n";
    public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";

    public SpringBootI18nConfig() {

    }

    protected static void initializeCubeFormatters(IActivePivotContentServiceConfig apContentServiceConfig) {
        initializeCubeFormatters(apContentServiceConfig.activePivotContentService().getContentService().withRootPrivileges());
    }

    protected static void initializeCubeFormatters(IContentService rootContentService) {
        IContentEntry languageDir = rootContentService.get("i18n");
        if (null != languageDir) {
            IPlugin<ICubeFormatterFactory> factories = Registry.getPlugin(ICubeFormatterFactory.class);
            Map<String, IContentEntry> languages = rootContentService.listDir("i18n", 1, false);
            Iterator var4 = languages.entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry<String, IContentEntry> e = (Map.Entry)var4.next();
                if (!((IContentEntry)e.getValue()).isDirectory()) {
                    String path = (String)e.getKey();
                    String lang = path.substring(path.lastIndexOf(47) + 1);
                    if (factories.valueOf(new Object[]{lang}) != null) {
                        LOGGER.warning("There is already a factory registered for key: " + lang + ". Will replace with newly created one.");
                    }

                    IContentService langDirectory = new PrefixedContentService(path, rootContentService);
                    CubeFormatterFactory factory = new CubeFormatterFactory(lang, langDirectory, -1L, true);
                    factories.add(factory);
                }
            }

        }
    }

    public static void pushTranslations(IActivePivotContentService apContentService) {
        pushTranslations(apContentService.getContentService().withRootPrivileges());
    }

    protected static void pushTranslations(IContentService rootContentService) {
        if (!rootContentService.exists("i18n")) {
            rootContentService.createDirectory("i18n", ROLE_ROOT_LIST, ROLE_ROOT_LIST, false);
        }

        try {
            push(new PrefixedContentService("i18n", rootContentService), "i18n");
        } catch (Exception var2) {
            throw new QuartetRuntimeException(var2);
        }
    }

    private static URI getDirectory(String directory) {
        URI dirUri;
        try {
            dirUri = QfsFiles.getResourceUrl(directory).toURI();
        } catch (MalformedURLException | URISyntaxException var3) {
            throw new QuartetRuntimeException("Cannot find the directory " + directory, var3);
        }

        if (dirUri == null) {
            throw new IllegalArgumentException("No directory named " + dirUri);
        } else {
            return dirUri;
        }
    }

    protected static void push(final PrefixedContentService contentService, final String languageFolder)
            throws ServiceException, IOException {

        final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        LOGGER.info("Scanning for languages in: " + languageFolder);
        final Resource[] resources = resolver.getResources(languageFolder + "/*");
        LOGGER.info("Found " + resources.length + " matching resources");
        for (final Resource resource : resources) {
            LOGGER.fine(resource.getDescription());
            final String name = resource.getFilename();
            LOGGER.info("Found language file " + name);
            if (contentService.exists(name)) {
                contentService.remove(name);
            }
            contentService.createFile(name, read(resource.getInputStream()), ROLE_ROOT_LIST, ROLE_ROOT_LIST, false);
        }

    }

    protected static String read(InputStream in) throws IOException {
        Scanner s = new Scanner(in, "UTF-8");
        Throwable var2 = null;

        String var3;
        try {
            var3 = s.useDelimiter("\\Z").next();
        } catch (Throwable var12) {
            var2 = var12;
            throw var12;
        } finally {
            if (s != null) {
                if (var2 != null) {
                    try {
                        s.close();
                    } catch (Throwable var11) {
                        var2.addSuppressed(var11);
                    }
                } else {
                    s.close();
                }
            }

        }

        return var3;
    }

    protected static ICanHaveRoleEntitlements.IHasAtLeastOneEntitlement roleAdmin(final ICanHaveRoleEntitlements.ICanStartBuildingEntitlement builder) {
        return builder
                .withGlobalEntitlement()
                .forRole(ROLE_ADMIN)
                .withMdxContext()
                .withCubeFormatter("en-US")
                .end();
    }

}
