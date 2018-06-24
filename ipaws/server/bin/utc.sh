readonly utcTimeFormat="%Y-%m-%dT%H:%M:%SZ"
readonly utcTimeGlob="[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]Z"

utcCurrentTime() {
   date -u +"${utcTimeFormat}"
} && readonly -f utcCurrentTime

utcFormatTime() {
   local time="${1}"

   date -u -d "${time}" +"${utcTimeFormat}"
} && readonly -f utcFormatTime

