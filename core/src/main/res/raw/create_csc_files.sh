#!/system/bin/sh
export PATH=/system/bin:$PATH

mount -o rw,remount /system
mount -t rootfs -o remount,rw rootfs

if [ ! -d /system/csc ]; then
mkdir /system/csc
fi

chmod 0777 /system/csc

if [ ! -f /system/csc/feature.xml.bak ]; then
cp -f /system/csc/feature.xml /system/csc/feature.xml.bak
fi

if [ ! -f /system/csc/feature.xml ]; then
touch /system/csc/feature.xml
fi

if [ ! -f /system/csc/sales_code.dat ]; then
  echo BTU > /system/csc/sales_code.dat
fi

chmod 0777 /system/csc/feature.xml
chmod 0777 /system/csc/feature.xml.bak
chmod 0777 /system/csc/others.xml
chmod 0777 /system/csc/others.xml.bak
chmod 0777 /system/csc/sales_code.dat
