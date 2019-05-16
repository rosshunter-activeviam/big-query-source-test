var baseUrl = window.location.href.split('/ui')[0];

window.env = {
  serverUrls: {
    activePivot: window.location.protocol + '//' + window.location.hostname + ':' + window.location.port,
    activeMonitor: window.location.protocol + '//' + window.location.hostname + ':'  + window.location.port,
    content: window.location.protocol + '//' + window.location.hostname + ':' + window.location.port'
  }
};