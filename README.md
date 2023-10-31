# Android Scheduling App C196

## Any required images can be found in the images folder

## Development Device Info

- avd.ini.displayname      Pixel_3a_API_34_extension_level_7_x86_64
- avd.ini.encoding         UTF-8
- AvdId                    Pixel_3a_API_34_extension_level_7_x86_64
- disk.dataPartition.size  6442450944
- hw.accelerometer         yes
- hw.arc                   false
- hw.audioInput            yes
- hw.battery               yes
- hw.camera.back           emulated
- hw.camera.front          emulated
- hw.cpu.ncore             12
- hw.device.hash2          MD5:0e6953ebf01bdc6b33a2f54746629c50
- hw.device.manufacturer   Google
- hw.device.name           pixel_3a
- hw.dPad                  no
- hw.gps                   yes
- hw.gpu.enabled           yes
- hw.gpu.mode              auto
- hw.keyboard              yes
- hw.lcd.density           440
- hw.lcd.height            2220
- hw.lcd.width             1080
- hw.mainKeys              no
- hw.ramSize               1536
- hw.sdCard                yes
- hw.sensors.orientation   yes
- hw.sensors.proximity     yes
- hw.trackBall             no
- image.androidVersion.api 34
- image.sysdir.1           system-images\android-34\google_apis\x86_64\
- PlayStore.enabled        false
- runtime.network.latency  none
- runtime.network.speed    full
- snapshot.present         no
- tag.display              Google APIs
- tag.id                   google_apis
- vm.heapSize              256

## Section F

### F1

If the application were designed for a tablet rather than a phone I would have designed the layout around a grid design rather than the linear layout. I would also increase the size of the card fragments and arrange them in a `2` or `3` by `n` grid.

### F2

This application supports a `minimum` operating system version of `Android 8.0 (Oreo)`. The application was developed using an emulator running `Android API 34`.

### F3

The challenges I faced during the development of the application were plentiful. I had several issues with connecting to my development device, which was strange. I was also very challenged with the Android methodology. I'm most familiar with ASP.NET and React, which are kind of similar now that I know more, so learning a new framework was difficult. Another thing I found challenging was the interpretation of the rubric. Some instructions seemed unclear until further discussion with the course instructor. The organization of the document could also be improved.

### F4

To overcome the previously stated challenges, I had to do a lot of system debugging. There was a period where the daemon would no longer connect to my development device. I tried many different steps to resolve the issue including:

- Uninstalling Android Studio
- Removing all Android related system files
- Installing an older version of Android studio
- Writing a system script to find and remove any Android related files

In the end none of that worked, and I had to reinstall Windows in order to get back to development.
In order to overcome the issues I had with learning Android, I spent a lot of time watching tutorial videos and finding StackOverflow articles. Overcoming the issues I encountered with the way that I interpreted the requirements was done through perseverance and assistance from my course instructor.

### F5

If I were to do this project again I would **not** go off and make my own calendar application again. I wrote a calendar view that would display occurring terms, courses, and assessments as I thought that was the 'scheduler' defined as part of section C. I would instead spend more time meeting with my course instructor getting the help I needed to better understand some of the requirements.

### F6

Positives of Emulators:

- Cost-Efficiency: Emulators are often more cost-effective than purchasing and maintaining multiple physical devices, especially when you need to support a wide range of platforms.
- Ease of Debugging: Emulators often offer robust debugging tools, making it easier to identify and resolve issues during development.
- Convenience: Emulators allow developers to work from a single machine, which is especially advantageous for remote or distributed development teams.

Negatives of Emulators:

- Limited Real-World Testing: Emulators may not perfectly replicate real-world hardware and software interactions, leading to discrepancies in performance, behavior, or compatibility.
- Resource Intensive: Running emulators can be resource-intensive on the host machine, leading to performance bottlenecks and sometimes requiring high-end hardware.
- Limited Hardware Interaction: For certain types of development (e.g., IoT devices), emulators may not provide access to the necessary hardware interfaces or sensors.
