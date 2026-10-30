import pathlib,subprocess,os,tempfile,json
r=pathlib.Path(__file__).resolve().parent
pf=pathlib.Path(tempfile.mkdtemp(prefix='qryptana-promotion-candidate-'))
for n in ['config','data','cache','tmp','prefs','home']:(pf/n).mkdir()
xd=(r/'display-path.txt').read_text().strip()
env=os.environ.copy();env.pop('LD_PRELOAD',None)
env.update(DISPLAY=':97',XAUTHORITY=xd+'/Xauthority',XDG_CONFIG_HOME=str(pf/'config'),XDG_DATA_HOME=str(pf/'data'),XDG_CACHE_HOME=str(pf/'cache'),JAVA_TOOL_OPTIONS=f'-Duser.home={pf}/home -Djava.io.tmpdir={pf}/tmp -Djava.util.prefs.userRoot={pf}/prefs')
with (r/'candidate.stdout').open('w') as out,(r/'candidate.stderr').open('w') as err:
 p=subprocess.Popen([str(r/'linux-temurin/main/app/qryptana/bin/qryptana')],env=env,stdout=out,stderr=err)
 (r/'candidate-process.json').write_text(json.dumps({'pid':p.pid,'profile':str(pf)},indent=2))
 print('Fresh disposable candidate pid',p.pid,flush=True)
 try:p.wait(timeout=500)
 finally:
  if p.poll() is None:p.terminate();p.wait(timeout=10)
 (r/'candidate-exit.json').write_text(json.dumps({'exit':p.returncode}))
