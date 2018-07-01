includeScriptLibraries xml time CAP

readonly filterTime="$(utcTime)"
logDebug "filter-time ${filterTime}"
readonly filterSeconds="$(epochSeconds "${filterTime}")"

acceptAlert() {
   local file="${1}"
   local remove="${2:-false}"

   [ -n "${alertIdentifier}" ] || {
      local alertIdentifier="$(xmlEvaluate "${file}" "/alert/identifier/text()")"

      [ -n "${alertIdentifier}" ] || {
         logWarning "missing identifier: ${file}"
         return 1
      }
   }

   handleAlertMessageType "${file}" "${remove}" &&
   handleAlertExpiry "${file}" &&
   processFilterConfigurationFile "${file}" &&
   return 0 || return 1
} && readonly -f acceptAlert

handleAlertMessageType() {
   local file="${1}"
   local remove="${2:-false}"

   local type="$(xmlEvaluate "${file}" "/alert/msgType/text()")"
   [ -n "${type}" ] || {
      logWarning "missing message type: ${alertIdentifier}"
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
         logWarning "unrecognized message type: ${type}: ${alertIdentifier}"
         return 1
      fi

      local references="$(xmlEvaluate "${file}" "/alert/references/text()")"
      [ -n "${references}" ] || {
         logWarning "${noun} with no references: ${alertIdentifier}"
         return 1
      }

      set -- ${references//,/ }
      local sender="${1}"
      local identifier="${2}"
      local sent="${3}"

      [ -n "${identifier}" ] || {
         logWarning "${noun} with no referenced identifier: ${alertIdentifier}"
         return 1
      }

      logInfo "alert withdrawn: ${identifier}: ${action} by ${alertIdentifier}"

      "${remove}" && {
         local path="${dataDirectory}/${identifier}.${alertFileExtension}"

         [ -f "${path}" ] && {
            logInfo "removing alert: ${identifier}"
            rm -f -- "${path}"
         }
      }
   }

   return 0
} && readonly -f handleAlertMessageType

handleAlertExpiry() {
   local file="${1}"

   local sentTime="$(xmlEvaluate "${file}" "/alert/sent/text()")"
   [ -n "${sentTime}" ] || {
      logInfo "no sent time: ${alertIdentifier}"
      return 1
   }

   local expiryTime="$(xmlEvaluate "${file}" "/alert/info/expires/text()")"
   [ -n "${expiryTime}" ] || {
      local effectiveTime="$(xmlEvaluate "${file}" "/alert/info/effective/text()")"
      [ -n "${effectiveTime}" ] || effectiveTime="${sentTime}"
      expiryTime="$(utcTime "${effectiveTime} -1 day")"
   }

   local expirySeconds="$(epochSeconds "${expiryTime}")"
   [ "${expirySeconds}" -lt "${filterSeconds}" ] && {
      logInfo "alert expired: ${alertIdentifier}: ${expiryTime}"
      return 1
   }

   return 0
} && readonly -f handleAlertExpiry

processFilterConfigurationFile() {
   local file="${1}"

   local configurationFile="${configurationDirectory}/ipaws.filter"
   [ -f "${configurationFile}" ] && [ -r "${configurationFile}" ] && {
      local line configurationLine=0

      while read -r line
      do
         let configurationLine+=1

         set -- ${line%%#*}
         [ "${#}" -eq 0 ] && continue
         local field="${1}"
         shift 1

         case "${field}"
         in
            status) acceptDiscreteField "${file}" "/alert/status" "${@}" || return 1;;
            urgency) acceptEnumeratedField "${file}" "/alert/info/urgency" capUrgencyEnumeration "${@}" || return 1;;
            severity) acceptEnumeratedField "${file}" "/alert/info/severity" capSeverityEnumeration "${@}" || return 1;;
            certainty) acceptEnumeratedField "${file}" "/alert/info/certainty" capCertaintyEnumeration "${@}" || return 1;;
            *) logFilterConfigurationProblem "unrecognized filter field" "${field}";;
         esac
      done <"${configurationFile}"
   }

   return 0
} && readonly -f processFilterConfigurationFile

acceptDiscreteField() {
   local file="${1}"
   local element="${2}"
   shift 2

   local text="$(xmlEvaluate "${file}" "${element}/text()")"
   [ -n "${text}" ] || {
      logWarning "element not defined: ${element}: ${alertIdentifier}"
      return 1
   }

   local value
   for value
   do
      [ "${text}" = "${value}" ] && return 0
   done

   logWarning "value not accepted: ${element}=${text}: ${alertIdentifier}"
   return 1
} && readonly -f acceptDiscreteField

acceptEnumeratedField() {
   local file="${1}"
   local element="${2}"
   local -n enumeration="${3}"
   local threshold="${4}"

   local text="$(xmlEvaluate "${file}" "${element}/text()")"
   [ -n "${text}" ] || {
      logWarning "element not defined: ${element}: ${alertIdentifier}"
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
      logWarning "unrecognized value: ${element}=${text}: ${alertIdentifier}"
      return 1
   }

   [ "${actual}" -le "${maximum}" ] || {
      logWarning "value not accepted: ${element}=${text}: ${alertIdentifier}"
      return 1
   }

   return 0
} && readonly -f acceptEnumeratedField

logFilterConfigurationProblem() {
   local problem="${1}"
   local data="${2}"

   logWarning "${problem}: ${data}: ${configurationFile}[${configurationLine}]"
} && readonly -f logFilterConfigurationProblem

