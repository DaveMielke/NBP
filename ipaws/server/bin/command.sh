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

importCommandProperties() {
   importProperties commandProperties "${configurationDirectory}/${programName%%-*}.conf"
} && readonly -f importCommandProperties

