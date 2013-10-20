## Midao JDBC 0.9.0 RC (May 31, 2013)
 - Java 1.5/1.6 support with one jar.
 - Functional tests with older JDBC drivers.
 - C3P0 pooled datasource support.
 - Removal of one and only strict dependency - slf4j. Now it is optional: if it is present in classpath - it would be used, otherwise Java logging would be used.
 - Minor changes in Maven pom.xml files.
 - Removed some old code and fixed issue related to it.
 
## Midao JDBC 0.9.1 RC (June 6, 2013)
 - **Needed to clarify that midao - is not a library, but midao-jdbc - is. Therefore renamed packages(org.midao.core->org.midao.jdbc.core) and updated module/file names**
 - **Added Microsoft SQL support for both JDBC3 and JDBC4.**
 - **Added Microsoft SQL functional tests (20+).**
 - Base statement handler improvements.
 - Base type handler improvements: now allows processing of output InputStream/Reader.
 - Introduction (technically rewrite) of new type handler. It is called Universal type handler as it is supported by both JDBC3/JDBC4 and MsSql, MySQL, PostgreSQL.
 - BaseMetadataHandler enhancement.
 - QueryParameters enhancement.
 - QueryRunner, BaseTypeHandler and QueryParameters test update.
 - Now system is more flexible: if metadata report 'return' field but it wasn't utilized in SQL String - system won't demand that field to be supplied (as out parameter). Applicable only for call with named input handler.
 - Improvements to TypeHandler utils.
 
## Midao JDBC 0.9.2 RC (June 20, 2013)
 - **Implemented Spring Exception handler. It includes all three types: JDBC4 exception translator, vendor independent sql state prefix translator and vendor specific translation**
 - **Implemented Lazy Statement handler, introduces new type of output handlers: Lazy type handlers. Currently only read-only forward mode is supported. Updatable and scrollable mode would be implemented in future versions.**
 - Added extensive unit tests for Spring Exception handler.
 - Added MetadataUtils. In future it is planned to move more functions there from BaseMetadataHandler.
 - Small changes to ExceptionUtils and it's unit tests.
 - Small changes to Base Exception handler and it's unit test.
 - Minor changes in AbstractQueryRunner. Now it attaches Exception handler instance per QueryRunner as it does with Metadata/Transaction/Type../ handlers.
 - Base/Universal Statement handler improvements.
 - Query output processor minor modifications.
 - Addition of few functional tests across all databases.
 - Introduction of LazyStatementHandler. It is now default choice for statement handling.
 - Introduction of two LazyOutputHandlers: BeanListLazyOutputHandler and MapListLazyOutputHandler (can be used only with LazyStatementHandler).
 - QueryParameters minor improvement (now allows to store processed output(return)).
 - Basic output handler minor improvements.
 - Few other small changes...
 
## Midao JDBC 0.9.3 (July 21, 2013)
 - **[Implemented scrollable and updateable Output Handlers] (http://midao.org/mjdbc-other-features.html#lazy-scroll-update)**
 - Improved Lazy output handlers hierarchy - now all Lazy handlers are in **lazy** package. Also renamed BeanListLazyOutputHandler and MapListLazyOutputHandler into BeanLazyOutputHandler and MapLazyOutputHandler.
 - Added 6 new Lazy output handlers: [BeanLazyScrollOutputHandler, BeanLazyScrollUpdateOutputHandler, BeanLazyUpdateOutputHandler, MapLazyScrollOutputHandler, MapLazyScrollUpdateOutputHandler, MapLazyUpdateOutputHandler] (http://midao.org/mjdbc-io-handlers.html#lazy-output-handler).
 - Small internal improvements in all Lazy output handlers.
 - Added LazyCacheIterator. Is returned from (QueryParametersLazyList).getLazyCacheIterator().
 - Added 2 new functional tests per each Database.
 - Updated c3p0 and dbcp dependencies. Delivered [c3p0(0.9.1) and dbcp(1.4.1 and 1.3.1)] (http://midao.org/mjdbc-other-features.html#pooled-datasource).
 - _Stopped adding RC to the version. Due to unplanned development - it is no longer can be considered as Release Candidate._
 
## Midao JDBC 0.9.4 (August 1, 2013)
 - **Maintenance release**
 - **[Added presentation] (http://midao.org/mjdbc/MJDBC_Presentation.pdf)**
 - **[Added getting started guide] (http://midao.org/mjdbc-getting-started.html)**
 - A lot of internal refactoring related to synchronization of class names amount other libraries of Midao Project: [Midao* classes were replaced by Mjdbc*] (http://midao.org/mjdbc/javadoc/org/midao/jdbc/core/package-summary.html). Midao* classes are now deprecated.
 - **[Midao exceptions were renamed into Mjdbc exceptions] (http://midao.org/mjdbc/javadoc/org/midao/jdbc/core/exception/package-summary.html)**
 - Addition in Examples.
 
## Midao JDBC 0.9.5 (October, 2013)
 - **Modified how query input parameters were handled - parameters and order are treated separately which allows to use one parameter more than once (but direction still is set per parameter not per position).**
 - **Added XML input handler which reads xml and executes it (handles both input and output): [XmlInputOutputHandler] (http://midao.org/mjdbc-io-handlers.html#xml-handler).**
 - **Addition of 4 new input handlers: [BeanInputHandlerEx, BeanListInputHandlerEx, MapInputHandlerEx, MapListInputHandlerEx] (http://midao.org/mjdbc-io-handlers.html#extended-input-handler).
 - Minor improvements for Overrider class.
 - Improvement to QueryParameters and ProcessedInput.
 - Added few new examples in [Examples project] (https://github.com/pryzach/midao/tree/master/midao-jdbc-examples/src/main/java/org/midao/jdbc/examples).
 - Increased amount of tests to 900.