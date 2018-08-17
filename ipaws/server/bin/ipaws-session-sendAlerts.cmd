ipawsSession_sendAlerts() {
   "${ipawsSendingAlerts}" || {
      ipawsSendingAlerts=true
      ipawsMonitorAlerts
   }
}

