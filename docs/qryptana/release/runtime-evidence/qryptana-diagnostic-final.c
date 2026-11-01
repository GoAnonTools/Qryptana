#define _GNU_SOURCE
#include <dlfcn.h>
#include <execinfo.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <sys/syscall.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/resource.h>
static int logfd(void){char p[512]; const char *d=getenv("QRYPTANA_DIAG_DIR");if(!d)return -1;snprintf(p,sizeof p,"%s/process-%d.log",d,getpid());return open(p,O_WRONLY|O_CREAT|O_APPEND,0600);}
static void mark(const char *event,const char *arg){int f=logfd();if(f<0)return;char exe[1024]={0};readlink("/proc/self/exe",exe,sizeof(exe)-1);dprintf(f,"pid=%d ppid=%d exe=%s event=%s arg=%s\n",getpid(),getppid(),exe,event,arg?arg:"");close(f);}
__attribute__((constructor)) static void init(void){mark("loaded","");}
pid_t fork(void){pid_t(*real)(void)=dlsym(RTLD_NEXT,"fork");pid_t r=real();if(r==0){mark("fork-child",""); char path[512];snprintf(path,sizeof path,"%s/child-%d.stderr",getenv("QRYPTANA_DIAG_DIR"),getpid());int f=open(path,O_WRONLY|O_CREAT|O_APPEND,0600);if(f>=0){dup2(f,2);close(f);}}return r;}
int execve(const char *p,char *const a[],char *const e[]){mark("execve",p);int(*real)(const char*,char*const[],char*const[])=dlsym(RTLD_NEXT,"execve");return real(p,a,e);}
void __cxa_pure_virtual(void){mark("PURE_VIRTUAL","");int f=logfd();void *b[40];int n=backtrace(b,40);if(f>=0){backtrace_symbols_fd(b,n,f);close(f);}void *h=dlopen("libstdc++.so.6",RTLD_NOLOAD|RTLD_LAZY);void(*real)(void)=h?dlvsym(h,"__cxa_pure_virtual","CXXABI_1.3"):0;if(!real){mark("resolution-failed",dlerror());__builtin_trap();}real();__builtin_unreachable();}
void abort(void){mark("ABORT","");int f=logfd();void *b[40];int n=backtrace(b,40);if(f>=0){backtrace_symbols_fd(b,n,f);close(f);}void(*real)(void)=dlsym(RTLD_NEXT,"abort");real();__builtin_unreachable();}

pid_t waitpid(pid_t p,int *st,int o){pid_t(*r)(pid_t,int*,int)=dlsym(RTLD_NEXT,"waitpid");pid_t n=r(p,st,o);char msg[100];snprintf(msg,sizeof msg,"child=%d status=%d",n,st?*st:-1);mark("waitpid",msg);return n;}

pid_t wait(int *st){pid_t(*r)(int*)=dlsym(RTLD_NEXT,"wait");int local=0;pid_t n=r(st?st:&local);char msg[100];snprintf(msg,sizeof msg,"child=%d status=%d",n,st?*st:local);mark("wait",msg);return n;}
pid_t wait4(pid_t p,int *st,int o,struct rusage *ru){pid_t(*r)(pid_t,int*,int,struct rusage*)=dlsym(RTLD_NEXT,"wait4");pid_t n=r(p,st,o,ru);char msg[100];snprintf(msg,sizeof msg,"child=%d status=%d",n,st?*st:-1);mark("wait4",msg);return n;}

int execvp(const char *p,char *const a[]){mark("execvp",p);int(*r)(const char*,char*const[])=dlsym(RTLD_NEXT,"execvp");return r(p,a);}
