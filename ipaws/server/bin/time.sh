readonly utcTimeFormat="%Y-%m-%dT%H:%M:%SZ"
readonly utcTimeGlob="[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]Z"

utcTime() {
   local time="${1}"

   [ -n "${time}" ] || time="now"
   date -u -d "${time}" +"${utcTimeFormat}"
} && readonly -f utcTime

epochSeconds() {
   local time="${1}"

   [ -n "${time}" ] || time="now"
   date -d "${time}" +"%s"
} && readonly -f epochSeconds

epochToUTC() {
   local seconds="${1}"

   utcTime "@${seconds}"
} && readonly -f epochToUTC

