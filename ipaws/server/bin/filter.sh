includeScriptLibraries xml time

readonly filterTime="$(utcTime)"
logDebug "filter-time ${filterTime}"
readonly filterSeconds="$(epochSeconds "${filterTime}")"

acceptAlert() {
   local file="${1}"

   xmlNodeExists "${file}" "/alert" || {
      logWarning "not an alert: ${file}"
      return 1
   }

   local alertIdentifier="$(xmlEvaluate "${file}" "/alert/identifier/text()")"
   [ -n "${alertIdentifier}" ] || {
      logWarning "missing identifier: ${file}"
      return 1
   }

   handleAlertMessageType "${file}" || return 1
   hasAlertExpired "${file}" && return 1

   local configurationFile="${configurationDirectory}/ipaws.filter"
   [ -f "${configurationFile}" ] || return 0

   local line lineNumber

   while read -r line
   do
      let lineNumber+=1

      set -- ${line%%#*}
      [ "${#}" -eq 0 ] && continue
      local field="${1}"
      shift 1

      case "${field}"
      in
         status) checkDiscreteField "${file}" "/alert/status" "${@}" || return 1;;
         urgency) checkEnumeratedField "${file}" "/alert/info/urgency" capUrgencyEnumeration "${@}" || return 1;;
         severity) checkEnumeratedField "${file}" "/alert/info/severity" capSeverityEnumeration "${@}" || return 1;;
         certainty) checkEnumeratedField "${file}" "/alert/info/certainty" capCertaintyEnumeration "${@}" || return 1;;
         *) logFilterConfigurationProblem "unrecognized filter field" "${field}";;
      esac
   done <"${configurationFile}"

   return 0
} && readonly -f acceptAlert

handleAlertMessageType() {
   local file="${1}"

   local type="$(xmlEvaluate "${file}" "/alert/msgType/text()")"
   [ -n "${type}" ] || {
      logWarning "missing message type: ${file}"
      return 1
   }

   [ "${type}" = "Alert" ] || {
      local noun action

      if [ "${type}" = "Update" ]
      then
         noun="update"
         action="replaced"
      elif [ "${type}" = "Cancel" ]
      then
         noun="cancellation"
         action="cancelled"
      else
         logWarning "unrecognized message type: ${type}: ${file}"
         return 1
      fi

      local references="$(xmlEvaluate "${file}" "/alert/references/text()")"
      [ -n "${references}" ] || {
         logWarning "${noun} with no references: ${file}"
         return 1
      }

      set -- ${references//,/ }
      local sender="${1}"
      local identifier="${2}"
      local sent="${3}"

      [ -n "${identifier}" ] || {
         logWarning "${noun} with no referenced identifier: ${file}"
         return 1
      }

      logInfo "removing obsolete alert: ${identifier}: ${action} by ${alertIdentifier}"
      rm -f -- "${dataDirectory}/${identifier}.${alertFileExtension}"
   }

   return 0
} && readonly -f handleAlertMessageType

hasAlertExpired() {
   local file="${1}"

   local sentTime="$(xmlEvaluate "${file}" "/alert/sent/text()")"
   [ -n "${sentTime}" ] || {
      logInfo "no sent time: ${file}"
      return 0
   }

   local expiryTime="$(xmlEvaluate "${file}" "/alert/info/expires/text()")"
   [ -n "${expiryTime}" ] || {
      local effectiveTime="$(xmlEvaluate "${file}" "/alert/info/effective/text()")"
      [ -n "${effectiveTime}" ] || effectiveTime="${sentTime}"
      expiryTime="$(utcTime "${effectiveTime} -1 day")"
   }

   local expirySeconds="$(epochSeconds "${expiryTime}")"
   [ "${expirySeconds}" -lt "${filterSeconds}" ] && {
      logInfo "alert has expired: ${alertIdentifier}: ${expiryTime}"
      return 0
   }

   return 1
} && readonly -f hasAlertExpired

checkDiscreteField() {
   local file="${1}"
   local element="${2}"
   shift 2

   local text="$(xmlEvaluate "${file}" "${element}/text()")"
   [ -n "${text}" ] || {
      logWarning "element not defined: ${element}: ${file}"
      return 1
   }

   local value
   for value
   do
      [ "${text}" = "${value}" ] && return 0
   done

   logWarning "value not allowed: ${element}=${text}: ${file}"
   return 1
} && readonly -f checkDiscreteField

checkEnumeratedField() {
   local file="${1}"
   local element="${2}"
   local -n enumeration="${3}"
   local threshold="${4}"

   local text="$(xmlEvaluate "${file}" "${element}/text()")"
   [ -n "${text}" ] || {
      logWarning "element not defined: ${element}: ${file}"
      return 1
   }

   [ -n "${threshold}" ] || {
      logFilterConfigurationProblem "missing configured threshold" "${element}"
      return 0
   }

   local maximum="${enumeration[${threshold}]}"
   [ -n "${maximum}" ] || {
      logFilterConfigurationProblem "unrecognized configured threshold" "${element}=${threshold}"
      return 0
   }

   local actual="${enumeration[${text}]}"
   [ -n "${actual}" ] || {
      logWarning "unrecognized value: ${element}=${text}: ${file}"
      return 1
   }

   [ "${actual}" -le "${maximum}" ] || {
      logWarning "value not allowed: ${element}=${text}: ${file}"
      return 1
   }

   return 0
} && readonly -f checkEnumeratedField

logFilterConfigurationProblem() {
   local problem="${1}"
   local data="${2}"

   logWarning "${problem}: ${data}: ${configurationFile}[${lineNumber}]"
} && readonly -f logFilterConfigurationProblem

