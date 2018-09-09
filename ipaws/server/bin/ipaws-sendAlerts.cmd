ipawsCommand_sendAlerts() {
   "${ipawsSendingAlerts}" || {
      ipawsSendingAlerts=true
      ipawsMonitorAlerts
   }
}

