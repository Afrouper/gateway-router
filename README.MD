> **ℹ️ Info:** Still in Development. Implementation contains still static dummy data. Will surely be removed 😉

# Gateway Router

Routing for multi tenant services in multi tenant environments.

## System IDs

* Each System has a unique ID
* Each System has a unique Base URI (or Host) to address

|System ID| URI                                 |
|---------|-------------------------------------|
|SYS_D1| https://dual-system-1.example.com   |
|SYS_D2| https://dual-system-2.example.com   |
|SYS_T1| https://tri-system-1.example.com    |
|SYS_T2| https://tri-system-2.example.com    |
|SYS_T3| https://tri-system-3.example.com    |
|XSYS_1| https://central-system.example.com  |
|SYS_Z1| https://binary-system-1.example.com |
|SYS_Z2| https://binary-system-2.example.com |

## Tenant IDs

* Each Tenant has a unique ID
* Each Tenant is placed on one one more Systems

| Tenant ID short | Tenant ID long | System IDs                     |
|-----------------|----------------|--------------------------------|
| 1001            | GBD267HX       | SYS_D1, SYS_T1, XSYS_1, SYS_Z1 |
| 1002            | GBD277HX       | SYS_D2, SYS_T2, XSYS_1, SYS_Z2 |
| 1500            | GBD277HX       | SYS_D2, SYS_T3, XSYS_1, SYS_Z2 |
| 4200            | GBD267HY       | SYS_D1, SYS_T1, XSYS_1, SYS_Z1 |

## Path Mapping

* Each Path has multiple (1-n) SystemIDs
* Each Path has a placeholder for the Tenant ID: {TENANT_ID}
  * Could also be a header field
* The Request will be routed to the Bse URI of the System assosiated with this Tenant ID

