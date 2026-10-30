import pathlib,subprocess,os,tempfile,json,sys
r=pathlib.Path(__file__).resolve().parent
variant,screen=sys.argv[1:3]
assert variant in ['before','after'] and screen in ['chatlist','settings','whatsnew']
app=(pathlib.Path('/home/theanarchitect/code/Qryptana-release-artifacts/toolchain-regression-20261030/qryptana-temurin/main/app/qryptana') if variant=='before' else r/'linux-temurin/main/app/qryptana')
pf=pathlib.Path(tempfile.mkdtemp(prefix='qryptana-promotion-preview-'))
for n in ['config','data','cache','tmp','prefs','home']:(pf/n).mkdir()
xd=(r/'display-path.txt').read_text().strip()
env=os.environ.copy();env.pop('LD_PRELOAD',None)
env.update(DISPLAY=':97',XAUTHORITY=xd+'/Xauthority',XDG_CONFIG_HOME=str(pf/'config'),XDG_DATA_HOME=str(pf/'data'),XDG_CACHE_HOME=str(pf/'cache'),JAVA_TOOL_OPTIONS=f'-Duser.home={pf}/home -Djava.io.tmpdir={pf}/tmp -Djava.util.prefs.userRoot={pf}/prefs')
label=variant+'-'+screen
cmd=['/home/theanarchitect/.local/share/qryptana-toolchains/jdk-21.0.13+9/bin/java','-Dskiko.library.path='+str(app/'lib/app'),'-Dcompose.application.resources.dir='+str(app/'lib/app/resources'),'-cp',str(r/'preview-classes')+':'+str(app/'lib/app/*'),'PromotionPreview',screen]
with (r/(label+'.stdout')).open('w') as out,(r/(label+'.stderr')).open('w') as err:
 p=subprocess.Popen(cmd,env=env,stdout=out,stderr=err)
 (r/(label+'-process.json')).write_text(json.dumps({'pid':p.pid,'profile':str(pf),'diagnostic_preview':True},indent=2))
 print(label,'pid',p.pid,flush=True)
 try:p.wait(timeout=500)
 finally:
  if p.poll() is None:p.terminate();p.wait(timeout=10)
 (r/(label+'-exit.json')).write_text(json.dumps({'exit':p.returncode}))
