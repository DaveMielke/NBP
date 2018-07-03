includeScriptLibraries locks

readonly serverDirectory="${programDirectory%/*}"

readonly defaultConfigurationDirectory="${serverDirectory}/etc"
readonly commandConfigurationFile="ipaws.conf"

readonly defaultDataDirectory="${serverDirectory}/var"
readonly failureFileExtension="failed"
readonly alertsFileExtension="alerts"
readonly alertFileExtension="alert"

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

processCommandConfigurationFile() {
   local directory="${1}"

   declare -g -A configuredProperties
   local file="${directory}/${commandConfigurationFile}"
   [ -f "${file}" ] && [ -r "${file}" ] || return 0
   local line number=0

   while read line
   do
      let number+=1
      set -- ${line%%#*}
      [ "${#}" -eq 0 ] && continue

      local name="${1}"
      local value="${2}"

      if [ "${#}" -eq 2 ]
      then
         configuredProperties["${name}"]="${value}"
      else
         local problem

         if [ "${#}" -eq 1 ]
         then
            problem="missing value"
         else
            problem="excess data"
         fi

         logWarning "${problem}: ${*}: ${file}[${number}]"
      fi
   done <"${file}"
} && readonly -f processCommandConfigurationFile

failIfAlreadyRunning() {
   local lock
   attemptExclusiveLock lock "${programName}" || semanticError "already running"
   pushOnExitCommand releaseLock "${lock}"
} && readonly -f failIfAlreadyRunning

waitIfAlreadyRunning() {
   local lock
   acquireExclusiveLock lock "${programName}"
   pushOnExitCommand releaseLock "${lock}"
} && readonly -f waitIfAlreadyRunning

