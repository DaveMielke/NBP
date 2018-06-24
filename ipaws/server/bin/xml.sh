verifyCommands xpath

readonly xpathAlertElement="/ns1:alerts/alert"

xpathGet() {
   local file="${1}"
   local xpath="${2}"

   xpath -q -e "${xpath}" "${file}"
} && readonly -f xpathGet

ipawsGetAlertCount() {
   local file="${1}"

   xpathGet "${file}" "count(${xpathAlertElement})"
} && readonly -f ipawsGetAlertCount

ipawsGetAlertElement() {
   local file="${1}"
   local index="${2}"

   xpathGet "${file}" "${xpathAlertElement}[${index}]"
} && readonly -f ipawsGetAlertElement

