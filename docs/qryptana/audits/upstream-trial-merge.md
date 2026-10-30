# H-07 upstream trial merge

Status: Partial — isolated trial executed; merge blocked by unrelated histories

The earlier run added `upstream` and recorded an unsuccessful fetch. In the corrective run, the existing remote was verified as:

`https://github.com/simplex-chat/simplex-chat.git`

`git ls-remote --tags upstream 'refs/tags/v6.5*'` succeeded. The latest plain stable v6.5.x tag advertised was **v6.5.6**, commit `59fce95d3cd08897b4ef742447b785cf2e56c7ce` (beta and armv7a variants excluded). `git fetch --no-tags upstream refs/tags/v6.5.6:refs/tags/v6.5.6` completed successfully without a shallow fetch. No 7.x work was begun.

Trial source: `127ec2ff706340b0b3351dad1d6abcab2f405dbe` on `release-readiness`, preserving the prior remediation history. Uncommitted corrections in the main worktree were not copied; this trial tests ancestry, not their eventual compatibility.

Commands in an isolated temporary worktree/throwaway branch:

```sh
git worktree add -b remediation-trial-v6.5.6 /tmp/qryptana-remediation-trial-v6.5.6 127ec2ff706340b0b3351dad1d6abcab2f405dbe
git -C /tmp/qryptana-remediation-trial-v6.5.6 -c core.hooksPath=/dev/null merge --no-commit --no-ff v6.5.6
```

Result: **exit 128, `fatal: refusing to merge unrelated histories`**. `git merge-base` also found no common ancestor. The local fork starts at imported snapshot `423c8c094aab585f52a47fe3956cf3b51559ab88`; fetching full upstream history does not connect it. No conflicted-file list exists because Git refused before merging; this is neither a clean merge nor a source conflict result. There was no merge state to abort. Only the trial worktree and throwaway branch were removed; the fetched tag and existing remotes remain.

NEEDS HUMAN: choose a future upstream integration strategy for the imported snapshot. No `--allow-unrelated-histories` merge, synthetic ancestor, history rewrite, or actual merge into `release-readiness` was attempted. The network blocker is resolved; ancestry/integration review remains.
