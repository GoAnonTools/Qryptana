# Security notes

## TLS test fixture

`tests/fixtures/tls/server.key` is an upstream-inherited, unencrypted PKCS#8 PEM private-key fixture. Its PEM header and a successful `openssl pkey -check -noout` parse establish its format; `file` misidentifies it as OpenSSH. `tests/ChatClient.hs` configures it as the server credential for both the test SMP server (`smpCredentials`) and the test XFTP server (`xftpCredentials`), together with `server.crt` and `ca.crt`. It is not a TLS client credential. It is present in the initial repository snapshot `423c8c0`; that establishes local provenance, not the original generation procedure.

It is publicly committed test material and must not be reused for a production service. The inspected references configure test servers, not deployment credentials; repository inspection cannot certify what external deployments use. Do not place credentials, deployment keys or personal keys in this fixture path. A future test-hygiene change may generate an ephemeral fixture at test setup, but this remediation does not remove, regenerate or expose the key.

## Reporting

Qryptana has not yet published a verified security contact. Security contact details and the final disclosure process are NEEDS HUMAN items before public release.
