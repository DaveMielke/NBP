includeScriptLibraries xml time CAP files

readonly filterTime="$(utcTime)"
logDebug "filter-time ${filterTime}"
readonly filterSeconds="$(epochSeconds "${filterTime}")"

cacheAlertProperties() {
   local file="${1}"

   cacheAlertProperty "${file}" type /alert/msgType
   cacheAlertProperty "${file}" references /alert/references

   cacheAlertProperty "${file}" sent /alert/sent
   cacheAlertProperty "${file}" effective /alert/info/effective
   cacheAlertProperty "${file}" expires /alert/info/expires

   cacheAlertProperty "${file}" status /alert/status
   cacheAlertProperty "${file}" certainty /alert/info/certainty
   cacheAlertProperty "${file}" severity /alert/info/severity
   cacheAlertProperty "${file}" urgency /alert/info/urgency

   cacheAlertProperty "${file}" event "/alert/info/eventCode[valueName/text()='SAME']/value"
   cacheAlertProperty "${file}" location "/alert/info/area/geocode[valueName/text()='SAME']/value"
} && readonly -f cacheAlertProperties

cacheAlertProperty() {
   local file="${1}"
   local property="${2}"
   local element="${3}"

   setFileAttribute "${file}" "nbp.ipaws.alert.${property}" "$(xmlEvaluate "${file}" "${element}/text()")"
} && readonly -f cacheAlertProperty

getAlertProperty() {
   local file="${1}"
   local property="${2}"

   getFileAttribute "${file}" "nbp.ipaws.alert.${property}"
} && readonly -f getAlertProperty

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

   handleAlertType "${file}" "${remove}" &&
   handleAlertExpiry "${file}" &&
   acceptConfiguredAlertProperties "${file}" &&
   return 0 || return 1
} && readonly -f acceptAlert

handleAlertType() {
   local file="${1}"
   local remove="${2:-false}"

   local type="$(getAlertProperty "${file}" type)"
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

      local references="$(getAlertProperty "${file}" references)"
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
} && readonly -f handleAlertType

handleAlertExpiry() {
   local file="${1}"

   local sentTime="$(getAlertProperty "${file}" sent)"
   [ -n "${sentTime}" ] || {
      logInfo "no sent time: ${alertIdentifier}"
      return 1
   }

   local expiryTime="$(getAlertProperty "${file}" expires)"
   [ -n "${expiryTime}" ] || {
      local effectiveTime="$(getAlertProperty "${file}" effective)"
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

acceptConfiguredAlertProperties() {
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
            status) acceptDiscreteAlertProperty "${file}" status "${@}" || return 1;;
            urgency) acceptEnumeratedAlertProperty "${file}" urgency capUrgencyEnumeration "${@}" || return 1;;
            severity) acceptEnumeratedAlertProperty "${file}" severity capSeverityEnumeration "${@}" || return 1;;
            certainty) acceptEnumeratedAlertProperty "${file}" certainty capCertaintyEnumeration "${@}" || return 1;;
            *) logFilterConfigurationProblem "unrecognized filter field" "${field}";;
         esac
      done <"${configurationFile}"
   }

   return 0
} && readonly -f acceptConfiguredAlertProperties

acceptDiscreteAlertProperty() {
   local file="${1}"
   local property="${2}"
   shift 2

   local value="$(getAlertProperty "${file}" "${property}")"
   [ -n "${value}" ] || {
      logWarning "property not defined: ${property}: ${alertIdentifier}"
      return 1
   }

   local candidate
   for candidate
   do
      [ "${value}" = "${candidate}" ] && return 0
   done

   logWarning "property value not accepted: ${property}=${value}: ${alertIdentifier}"
   return 1
} && readonly -f acceptDiscreteAlertProperty

acceptEnumeratedAlertProperty() {
   local file="${1}"
   local property="${2}"
   local -n enumeration="${3}"
   local threshold="${4}"

   local value="$(getAlertProperty "${file}" "${property}")"
   [ -n "${value}" ] || {
      logWarning "property not defined: ${property}: ${alertIdentifier}"
      return 1
   }

   [ -n "${threshold}" ] || {
      logFilterConfigurationProblem "missing configured threshold" "${property}"
      return 0
   }

   local maximum="${enumeration[${threshold}]}"
   [ -n "${maximum}" ] || {
      logFilterConfigurationProblem "unrecognized configured threshold" "${property}=${threshold}"
      return 0
   }

   local actual="${enumeration[${value}]}"
   [ -n "${actual}" ] || {
      logWarning "unrecognized property value: ${property}=${value}: ${alertIdentifier}"
      return 1
   }

   (( actual <= maximum )) && return 0
   logWarning "property value not accepted: ${property}=${value}: ${alertIdentifier}"
   return 1
} && readonly -f acceptEnumeratedAlertProperty

logFilterConfigurationProblem() {
   local problem="${1}"
   local data="${2}"

   logWarning "${problem}: ${data}: ${configurationFile}[${configurationLine}]"
} && readonly -f logFilterConfigurationProblem

