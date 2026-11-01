#include <linux/uinput.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <sys/ioctl.h>
static int fd;
static void e(int t,int c,int v){struct input_event x={0};x.type=t;x.code=c;x.value=v;if(write(fd,&x,sizeof x)!=sizeof x)exit(2);}
static void syncout(){e(EV_SYN,SYN_REPORT,0);usleep(50000);}
int main(int n,char**a){fd=open("/dev/uinput",O_WRONLY|O_NONBLOCK);if(fd<0){perror("uinput");return 1;}ioctl(fd,UI_SET_EVBIT,EV_KEY);ioctl(fd,UI_SET_KEYBIT,BTN_LEFT);for(int i=1;i<128;i++)ioctl(fd,UI_SET_KEYBIT,i);ioctl(fd,UI_SET_EVBIT,EV_REL);ioctl(fd,UI_SET_RELBIT,REL_X);ioctl(fd,UI_SET_RELBIT,REL_Y);ioctl(fd,UI_SET_RELBIT,REL_WHEEL);struct uinput_setup u={0};u.id.bustype=BUS_USB;u.id.vendor=0x1234;u.id.product=0x5678;strcpy(u.name,"Qryptana disposable runtime test input");if(ioctl(fd,UI_DEV_SETUP,&u)||ioctl(fd,UI_DEV_CREATE)){perror("setup");return 2;}sleep(1);
if(!strcmp(a[1],"click")){e(EV_REL,REL_X,-32767);e(EV_REL,REL_Y,-32767);syncout();e(EV_REL,REL_X,atoi(a[2]));e(EV_REL,REL_Y,atoi(a[3]));syncout();e(EV_KEY,BTN_LEFT,1);syncout();e(EV_KEY,BTN_LEFT,0);syncout();}
else if(!strcmp(a[1],"scroll")){e(EV_REL,REL_WHEEL,atoi(a[2]));syncout();}
else if(!strcmp(a[1],"key")){for(int i=2;i<n;i++){int k=atoi(a[i]);e(EV_KEY,k,1);syncout();e(EV_KEY,k,0);syncout();}}
sleep(1);ioctl(fd,UI_DEV_DESTROY);close(fd);return 0;}
