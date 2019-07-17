// Use this file to set your production environment variables.
// It is not bundled: you can change it without recompiling the app.
// Consequently, you should pay a particular attention to browser support:
// Unlike the rest of the application, this module will be sent as is to your users.

window.env = {
  serverUrls: {
    activePivot: window.location.protocol + '//' + window.location.hostname + ':9090',
    activeMonitor: window.location.protocol + '//' + window.location.hostname + ':9090',
    content: window.location.protocol + '//' + window.location.hostname + ':9090'
  }
};
