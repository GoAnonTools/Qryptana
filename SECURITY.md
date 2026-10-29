# Security notes

## TLS test fixture

`tests/fixtures/tls/server.key` is a committed upstream-inherited OpenSSH private-key fixture used only by the TLS client tests in `tests/ChatClient.hs` together with `server.crt` and `ca.crt`. Repository history shows it present from the initial snapshot, and `file` identifies it as an OpenSSH private key without a password.

It is not a production key, is not used by the application’s deployed relay configuration, and must not be reused for a service. Do not place credentials, deployment keys or personal keys in this fixture path. A future test-hygiene change may generate an ephemeral fixture at test setup, but this remediation does not remove, regenerate or expose the key.

## Reporting

Qryptana has not yet published a verified security contact. Security contact details and the final disclosure process are NEEDS HUMAN items before public release.
