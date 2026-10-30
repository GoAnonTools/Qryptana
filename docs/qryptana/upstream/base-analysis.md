# Upstream base analysis

Source: clean release-readiness at 2d5b1beff (abbreviated); authoritative full tip: `2d5b1beff6a359b2d88479aa4a4847a3c2859471`.
Remote verified as https://github.com/simplex-chat/simplex-chat.git. Existing trial failed on unrelated histories, before content merging. Eight release candidates compared; fetched explicit refs with --no-tags; no local tags rewritten.

| Candidate | Exact commit | Differing paths |
|---|---|---|
| v6.4.11 | `462e47bacd72968e418eb2a9942859535c040703` | 1347 |
| v6.5.0 | `d6c9c0ee47ede2df319eb131df5a5362a680f089` | 460 |
| v6.5.1 | `79d21f06bf3a8c4697993c7214d19d8694ac0264` | 433 |
| v6.5.2 | `1491f68cd204c279db23caf951916c81941852d5` | 310 |
| v6.5.3 | `1a82732f88af659bb197f73613b87763daeabd22` | 8 |
| v6.5.4 | `e92afb68d52bff0d2a035b3594d16badb3f58649` | 111 |
| v6.5.5 | `2d23c2f3921882e1746322710642e831bba9af4c` | 201 |
| v6.5.6 | `59fce95d3cd08897b4ef742447b785cf2e56c7ce` | 209 |

Best-supported base: v6.5.3, high confidence for reconstruction, not proof of the historical download source. All application/core/resources/binary blobs match except version metadata in two files. v6.5.2 differs in 310 paths, including core and binary documentation assets. The import retains 6.5.2 app counters with the 6.5.3 core. Dates were not used to establish identity.

Complete base-to-snapshot inventory (raw Git modes and blob IDs):

```
:000000 100644 0000000000000000000000000000000000000000 1c345e4a80db88079410e43ba4377be525926098 A	ATTRIBUTION.md
:000000 100644 0000000000000000000000000000000000000000 1bae5d9bb77467ffdd8b6e508224a5e8cb869766 A	QRYPTANA_SCOPE.md
:000000 100644 0000000000000000000000000000000000000000 d5ec3fc33cbf38da725727f9a1151763944ed0d5 A	UPSTREAM.md
:100644 100644 3d274bcc8503ad16ca47d9d331c37473181f0587 f0bd6d9118466480e28f755c9d9166ff1c7585fa M	apps/ios/SimpleX.xcodeproj/project.pbxproj
:100644 000000 a847d1268972855a7ffd22b7cdaa2b2a4218c62f 0000000000000000000000000000000000000000 D	apps/multiplatform/.idea/codeStyles/Project.xml
:100644 000000 79ee123c2b23e069e35ed634d687e17f731cc702 0000000000000000000000000000000000000000 D	apps/multiplatform/.idea/codeStyles/codeStyleConfig.xml
:100644 100644 3d4bf6691345f28586796b61d7c0857bdb77e019 4d504e069e94ae4c7a5046d97b73ac040dbf7f4f M	apps/multiplatform/gradle.properties
:000000 100644 0000000000000000000000000000000000000000 e69de29bb2d1d6434b8b29ae775ad8c2e48c5391 A	docs/qryptana/PLATFORM_STATUS.md

```

The added files are Qryptana attribution/scope/upstream documentation and an empty platform-status file. Two IDE code-style files are removed. Gradle app versions/counters and iOS app versions/counters/native archive references revert to 6.5.2 values. There are no protocol changes, binary changes, mode changes or submodule changes in this delta. Full recursive tree comparison includes all tracked strings/assets/modes/gitlinks.

Reconstruction: create new branch at v6.5.3; load the imported tree with git read-tree --reset -u 423c8c0 in the isolated worktree; commit as an explicit snapshot reconstruction. Commit c3c157aad has tree `f76a515a08edd03011c8039e6844384930a43dd5`, exactly equal to 423c8c0. No replace refs or graft metadata used. Derive replay order with git rev-list --reverse 423c8c0..release-readiness; 70 commits, no merge commits. Cherry-pick preserves original authors/messages. Replayed tip 478656a34 has tree `7e028e2d9d3e2a166500f3208df30e4cf5fec680`, exactly equal to release-readiness. Migration documents are added only after this equivalence checkpoint.
