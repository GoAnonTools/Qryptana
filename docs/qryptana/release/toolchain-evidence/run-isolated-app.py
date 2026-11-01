import os,pathlib,subprocess,json,sys,tempfile
r=pathlib.Path(__file__).resolve().parent
label=sys.argv[1]
pf=pathlib.Path(sys.argv[2]) if len(sys.argv)>2 else pathlib.Path(tempfile.mkdtemp(prefix='qryptana-temurin-'))
for d in ['config','data','cache','tmp','prefs']: (pf/d).mkdir(exist_ok=True)
env=os.environ.copy();env.pop('LD_PRELOAD',None)
env.update(DISPLAY=':98',XAUTHORITY='/tmp/qryptana-isolated-x11/Xauthority',XDG_CONFIG_HOME=str(pf/'config'),XDG_DATA_HOME=str(pf/'data'),XDG_CACHE_HOME=str(pf/'cache'),JAVA_TOOL_OPTIONS=f'-Djava.io.tmpdir={pf}/tmp -Djava.util.prefs.userRoot={pf}/prefs')
if label=='traced':
 td=r/'app-trace';td.mkdir(exist_ok=True)
 env.update(LD_PRELOAD='/tmp/qryptana-diagnostic-final.so',QRYPTANA_DIAG_DIR=str(td))
with (r/f'{label}.stdout').open('w') as out,(r/f'{label}.stderr').open('w') as err:
 p=subprocess.Popen([str(r/'qryptana-temurin/main/app/qryptana/bin/qryptana')],env=env,stdout=out,stderr=err)
 (r/f'{label}-process.json').write_text(json.dumps({'pid':p.pid,'profile':str(pf),'display':':98'},indent=2))
 print('Disposable UI pid',p.pid,'profile',pf,flush=True)
 try: p.wait(timeout=650)
 finally:
  if p.poll() is None:p.terminate();p.wait(timeout=10)
 (r/f'{label}-exit.json').write_text(json.dumps({'exit':p.returncode}))
