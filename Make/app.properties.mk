BUILD_PROPERTIES = res/values/properties.xml
LOCAL_FILES += $(BUILD_PROPERTIES)
properties: $(BUILD_PROPERTIES)
$(BUILD_PROPERTIES)::
	$(TOP)app.mkproperties >$@

