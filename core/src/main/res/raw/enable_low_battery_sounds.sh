#!/system/bin/sh
export PATH=/system/bin:$PATH

mount -o rw,remount /system
mount -t rootfs -o remount,rw rootfs

cp -f /system/media/audio/ui/LowBattery.ogg.bak /system/media/audio/ui/LowBattery.ogg
chmod 0644 /system/media/audio/ui/LowBattery.ogg
rm -r /system/media/audio/ui/LowBattery.ogg.bak
