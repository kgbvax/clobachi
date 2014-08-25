local function switch(val)
    devices.system.SetOut{output=1, value=val}
    devices.system.SetOut{output=2, value=val}
end

local function switchOff()
  switch(false)
end

local port=event.args.port;
local duration=event.args.duration;


switch(true)
delay(duration,switchOff)


