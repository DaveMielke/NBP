readonly serverDirectory="${programDirectory%/*}"

readonly defaultConfigurationDirectory="${serverDirectory}/etc"
readonly configurationFileName="ipaws.conf"

readonly defaultDataDirectory="${serverDirectory}/var"
readonly failureFileExtension="failed"
readonly alertsFileExtension="retrieved"

syntaxError() {
   local message="${1}"

   programMessage "${message}"
   exit 2
} && readonly -f syntaxError

semanticError() {
   local message="${1}"

   programMessage "${message}"
   exit 3
} && readonly -f semanticError

responseError() {
   local message="${1}"

   programMessage "${message}"
   exit 4
} && readonly -f responseError

processConfigurationFile() {
   local directory="${1}"

   declare -g -A configuredProperties
   local file="${directory}/${configurationFileName}"
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

         programMessage "${problem}: ${*}: ${file}[${number}]"
      fi
   done <"${file}"
} && readonly -f processConfigurationFile

