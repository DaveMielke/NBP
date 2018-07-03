set -e
shopt -s nullglob

readonly programName="${0##*/}"
programDirectory="${0%/*}"
[ "${programDirectory}" = "${programDirectory}" ] && programDirectory="."
readonly programDirectory="$(realpath "${programDirectory}")"

programMessage() {
   local message="${1}"

   [ -z "${message}" ] || {
      if [ -t 2 ]
      then
         message="${programName}: ${message}"
      else
         printf -v message "%(%Y-%m-%d@%H:%M:%S)T %s %s" -1 "${programName}" "${message}"
      fi

      echo >&2 "${message}"
   }
} && readonly -f programMessage

setVariable() {
   eval "${1}"'="'"${2}"'"'
} && readonly -f setVariable

defineEnumeration() {
   local array="${1}"
   shift 1

   eval declare -g -A "${array}=()"
   local -n array="${array}"

   local value=0
   local name

   for name
   do
      [ -z "${name}" ] && continue
      array[${name}]="${value}"

      while :
      do
         name="${name%?}"
         [ -z "${name}" ] && break

         local -n element="array[${name}]"
         element=$(((${#element} == 0)? value: -1))
         unset -n element
      done

      let value+=1
   done

   for name in ${!array[*]}
   do
      local -n element="array[${name}]"
      [ "${element}" -lt 0 ] && unset element
      unset -n element
   done

   readonly array
} && readonly -f defineEnumeration

defineEnumeration logLevelEnumeration debug info warning error
[ -n "${IPAWS_LOG_LEVEL}" ] || export IPAWS_LOG_LEVEL="${logLevelEnumeration["warning"]}"

logMessage() {
   local type="${1}"
   local message="${2}"
   local level="${logLevelEnumeration[${type}]}"

   if [ -z "${level}" ]
   then
      programMessage "unknown log type: ${type}: ${message}"
   elif [ "${level}" -ge "${IPAWS_LOG_LEVEL}" ]
   then
      programMessage "${message}"
   fi
} && readonly -f logMessage

logDebug() {
   local message="${1}"
   logMessage debug "${message}"
} && readonly -f logDebug

logInfo() {
   local message="${1}"
   logMessage info "${message}"
} && readonly -f logInfo

logWarning() {
   local message="${1}"
   logMessage warning "${message}"
} && readonly -f logWarning

logError() {
   local message="${1}"
   logMessage error "${message}"
} && readonly -f logError

setLogLevel() {
   local type="${1}"
   local level="${logLevelEnumeration[${type}]}"

   if [ -n "${level}" ]
   then
      IPAWS_LOG_LEVEL="${level}"
   else
      logWarning "unknown log type: ${type}"
   fi
} && readonly -f setLogLevel

verifyCommands() {
   local command

   for command
   do
      local path=$(type -p "${command}")
      [ -n "${path}" ] || semanticError "command not found: ${command}"
      logDebug "command-path ${path}"
   done
} && readonly -f verifyCommands

pushOnExitCommand() {
   local -n command="onExitCommand$((++onExitCommandCount))"
   command=("${@}")
} && readonly -f pushOnExitCommand

executeOnExitCommands() {
   while [ "${onExitCommandCount}" -gt 0 ]
   do
      local -n command="onExitCommand$((onExitCommandCount--))"
      "${command[@]}"
      unset -n command
   done
} && readonly -f executeOnExitCommands

onExitCommandCount=0
trap executeOnExitCommands exit

declare -g -A includedScriptLibraries=()
includeScriptLibraries() {
   local name

   for name
   do
      local -n included="includedScriptLibraries["${name}"]"

      [ -z "${included}" ] && {
         included=1
         . "${programDirectory}/${name}.sh"
      }

      unset -n included
   done
} && readonly -f includeScriptLibraries

