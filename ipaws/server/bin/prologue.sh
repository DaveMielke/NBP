set -e
shopt -s nullglob

readonly programName="${0##*/}"
programDirectory="${0%/*}"
[ "${programDirectory}" = "${0}" ] && programDirectory="."
readonly programDirectory="$(realpath "${programDirectory}")"
readonly rootDirectory="${programDirectory%/*}"

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
   local -n defEnum_array="${1}"
   shift 1

   declare -g -A "${!defEnum_array}=()"
   local defEnum_value=0
   local defEnum_name

   for defEnum_name
   do
      [ -n "${defEnum_name}" ] || continue
      defEnum_array["${defEnum_name}"]="${defEnum_value}"

      while :
      do
         defEnum_name="${defEnum_name%?}"
         [ -n "${defEnum_name}" ] || break
         defEnum_array["${defEnum_name}"]=$(((${#defEnum_array["${defEnum_name}"]} == 0)? defEnum_value: -1))
      done

      let defEnum_value+=1
   done

   for defEnum_name in "${!defEnum_array[@]}"
   do
      [ "${defEnum_array["${defEnum_name}"]}" -lt 0 ] && unset "defEnum_array["${defEnum_name}"]"
   done

   readonly defEnum_array
} && readonly -f defineEnumeration

defineEnumeration logLevelEnumeration debug info warning error
[ -n "${IPAWS_LOG_LEVEL}" ] || export IPAWS_LOG_LEVEL="${logLevelEnumeration["warning"]}"

logMessage() {
   local type="${1}"
   local message="${2}"
   local level="${logLevelEnumeration["${type}"]}"

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
   local level="${logLevelEnumeration["${type}"]}"

   if [ -n "${level}" ]
   then
      IPAWS_LOG_LEVEL="${level}"
   else
      logWarning "unknown log type: ${type}"
   fi
} && readonly -f setLogLevel

syntaxError() {
   local message="${1}"

   logError "${message}"
   exit 2
} && readonly -f syntaxError

semanticError() {
   local message="${1}"

   logError "${message}"
   exit 3
} && readonly -f semanticError

internalError() {
   local message="${1}"

   logError "${message}"
   exit 4
} && readonly -f internalError

verifyCommandAvailability() {
   local command

   for command
   do
      local path=$(type -p "${command}")
      [ -n "${path}" ] || semanticError "command not found: ${command}"
      logDebug "command-path ${path}"
   done
} && readonly -f verifyCommandAvailability

pushOnExitCommand() {
   local -n command="onExitCommand$((++onExitCommandCount))"
   command=("${@}")
} && readonly -f pushOnExitCommand

executeOnExitCommands() {
   while [ "${onExitCommandCount}" -gt 0 ]
   do
      local -n command="onExitCommand$((onExitCommandCount--))"
      logDebug "executing on exit command: ${command[*]}"
      "${command[@]}" || :
      unset -n command
   done
} && readonly -f executeOnExitCommands

onExitCommandCount=0
trap executeOnExitCommands exit

declare -g -A requiredScriptLibraries=()
requireScriptLibraries() {
   local name

   for name
   do
      [ -n "${requiredScriptLibraries["${name}"]}" ] || {
         requiredScriptLibraries["${name}"]=1
         . "${programDirectory}/${name}.lib"
      }
   done
} && readonly -f requireScriptLibraries

importConfigurationFile() {
   local -n icfArray="${1}"
   local icfFile="${2}"

   declare -g -A "${!icfArray}=()"
   [ -f "${icfFile}" ] && [ -r "${icfFile}" ] || return 0
   local icfLine icfNumber=0

   while read -r icfLine
   do
      let icfNumber+=1
      set -- ${icfLine%%#*}
      [ "${#}" -eq 0 ] && continue

      local icfName="${1}"
      local icfValue="${2}"

      if [ "${#}" -eq 2 ]
      then
         icfArray["${icfName}"]="${icfValue}"
      else
         local icfProblem

         if [ "${#}" -eq 1 ]
         then
            icfProblem="missing value"
         else
            icfProblem="excess data"
         fi

         logWarning "${icfProblem}: ${icfFile}[${icfNumber}]: ${*}"
      fi
   done <"${icfFile}"
} && readonly -f importConfigurationFile

