includeScriptLibraries xml

defineEnumeration capUrgencyEnumeration Immediate Expected Future Past Unknown
defineEnumeration capSeverityEnumeration Extreme Severe Moderate Minor Unknown
defineEnumeration capCertaintyEnumeration Observed Likely Possible Unlikely Unknown

readonly capAlertElement="/ns1:alerts/alert"

capGetAlertCount() {
   local file="${1}"

   xpathGet "${file}" "count(${capAlertElement})"
} && readonly -f capGetAlertCount

capGetAlertElement() {
   local file="${1}"
   local index="${2}"

   xpathGet "${file}" "${capAlertElement}[${index}]"
} && readonly -f capGetAlertElement

