declare -A lockDescriptorNames=()

findLockDescriptor() {
   local -n fldDescriptor="${1}"
   local name="${2}"
   local fd=10

   while :
   do
      [ -e "/dev/fd/${fd}" ] || break
      let fd+=1
   done

   eval exec "${fd}>>" '"${dataDirectory}/${name}.lock"'
   fldDescriptor="${fd}"
} && readonly -f findLockDescriptor

acquireLock() {
   local -n aqlDescriptor="${1}"
   local name="${2}"
   local exclusive="${3}"
   local wait="${4}"

   local typeWord typeOption
   if "${exclusive}"
   then
      typeWord="exclusive"
      typeOption="-e"
   else
      typeWord="shared"
      typeOption="-s"
   fi

   local actionWord actionOption
   if "${wait}"
   then
      actionWord="acquiring"
      actionOption=""
   else
      actionWord="attempting"
      actionOption="-n"
   fi

   findLockDescriptor aqlDescriptor "${name}"
   logDebug "${actionWord} ${typeWord} lock: ${name}"

   flock ${typeOption} ${actionOption} "${aqlDescriptor}" || {
      logDebug "lock failed: ${name}"
      return 1
   }

   lockDescriptorNames[${aqlDescriptor}]="${name}"
   logDebug "lock acquired: ${name}"
} && readonly -f acquireLock

acquireExclusiveLock() {
   local -n axlDescriptor="${1}"
   local name="${2}"

   acquireLock axlDescriptor "${name}" true true
} && readonly -f acquireExclusiveLock

acquireSharedLock() {
   local -n aslDescriptor="${1}"
   local name="${2}"

   acquireLock aslDescriptor "${name}" false true
} && readonly -f acquireSharedLock

attemptExclusiveLock() {
   local -n axlDescriptor="${1}"
   local name="${2}"

   acquireLock axlDescriptor "${name}" true false
} && readonly -f attemptExclusiveLock

attemptSharedLock() {
   local -n aslDescriptor="${1}"
   local name="${2}"

   acquireLock aslDescriptor "${name}" false false
} && readonly -f attemptSharedLock

releaseLock() {
   local descriptor="${1}"
   local -n name="lockDescriptorNames[${descriptor}]"

   if [ -n "${name}" ]
   then
      flock -u "${descriptor}"
      logDebug "lock released: ${name}"
      unset name
   else
      logWarning "lock not held: fd=${descriptor}"
   fi
} && readonly -f releaseLock

