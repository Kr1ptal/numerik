# NumeriK
#### Pluggable numeric wrappers for Kotlin

---



NumeriK is a pluggable numeric wrapper library written in Kotlin, with complete Java interop. It provides 
highly optimized and lightweight wrappers for different numeric types. This allows seamless switching
between the types, based on acceptable trade-offs in different environments. An example of where this is
beneficial are financial apps, where one might be willing to trade some accuracy for speed in development/testing 
environment, but would still like to have accurate results in production.

## DecimalNumber
Base class of all decimal number wrapper implementations. This is the class that should be used most of the time
if type switching is desired. Wrapper type can be set via `decimalnumber.type` system property. Possible 
options are `bigdecimal` (default) and `double`.

## BigDecimalNumber
`BigDecimal` wrapper. Scale can be set via `bigdecimal.scale`, which defaults to `12` decimal places. All
multiplication and division operations use the `HALF_EVEN` rounding mode.