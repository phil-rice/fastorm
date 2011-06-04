group MySql;

makeTemp(useTemporaryTable) ::= "<if(useTemporaryTable)>temporary<endif>"

dropTempTable(tempTableName,useTemporaryTable) ::= "drop  table if exists <tempTableName> "

createAllEntitiesTempTable(tempTableName,idColumn,idType,useTemporaryTable) ::= "create <makeTemp(useTemporaryTable)> table <tempTableName>(<idColumn> <idType> ) engine=memory"

createOneToManyTempTable (tempTableName,idColumn,idType,parentIdColumn,parentIdType, childLink,useTemporaryTable) ::= <<
   create  <makeTemp(useTemporaryTable)> table <tempTableName> ( <idColumn> <idType>, <childLink> <parentIdType>) engine=memory
>>

createManyToOneTempTable(tempTableName,idColumn,idType,parentIdColumn,parentIdType, parentLink,useTemporaryTable) ::= <<
   create  <makeTemp(useTemporaryTable)> table <tempTableName> ( <idColumn> <idType>, <parentLink> <parentIdType>) engine=memory
>>

addIndexToTempTable(tempTableName,idColumn) ::= "alter table <tempTableName> add index <tempTableName_idx> (<idColumn>)"

truncateTempTable(tempTableName) ::= "truncate  <tempTableName>"

populateAllEntitiesTempTable(tableName, tempTableName,idColumn,start,size) ::=<<
insert into <tempTableName> (<idColumn>) select <idColumn> from <tableName> limit <start>,<size>
>> 

populateOneToManyTempTable (parentTemp, tableName, tempTableName,idColumn,parentIdColumn,childLink)::= <<
insert into <tempTableName> (<idColumn>, <childLink>) 
     select r.<idColumn>, r.<childLink> from  <parentTemp> pt join <tableName> r on  pt.<parentIdColumn> = r.<childLink>
>>
   
populateManyToOneTempTable(parentTempTableName, tableName, tempTableName,idColumn,parentIdColumn,childLink)::= <<
insert into <tempTableName> (<idColumn>, <parentIdColumn>) 
       select real.<idColumn>, real.<childLink> from <tableName> real,<parentTempTable> parentTemp
            where parentTemp.<parentIdColumn> = <tableName>.<childLink>
>> 

drainPrimaryTable(tableName, tempTableName,idColumn) ::="select r.* from <tempTableName> t left outer join <tableName> r on r.<idColumn>=t.<idColumn>" 

drainSecondaryTable(tableName,tempTableName,idColumn) ::= "select r.* from <tempTableName> t left outer join <tableName> r on r.<idColumn>=t.<idColumn>"

drainLeafOneToManyTable(tableName,parentTemp,parentIdColumn,childLink) ::= "select r.* from  <parentTemp> pt join <tableName> r on  pt.<parentIdColumn> = r.<childLink>"

dropStoredProcedure(procName) ::= "drop procedure if exists <procName>"

createAllEntitiesStoredProcedure(procName, tableName, tempTableName,idColumn) ::= <<
   create procedure <procName>(theStart INT, theSize INT)
     begin
       <truncateTempTable(...)>;
       alter table <tempTableName> disable keys;
       prepare stmt from "insert into <tempTableName> (<idColumn>) select <idColumn> from <tableName> limit ?,?";
       set @start = theStart;
       set @size = theSize;
       execute stmt using @start,@size;
       alter table <tempTableName> enable keys;
       <drainPrimaryTable(...)>;
     end
>>
createOneToManyStoredProcedure(procName, parentTemp, tableName, tempTableName,idColumn,parentIdColumn,childLink) ::=<<
   create procedure <procName>()
     begin
       <truncateTempTable(...)>;  
       alter table <tempTableName> disable keys;
       <populateOneToManyTempTable(...)>;
       alter table <tempTableName> enable keys;
       <drainSecondaryTable(...)>;
     end
>>
createOneToManyLeafStoredProcedure(procName, parentTemp, tableName, tempTableName,idColumn,parentIdColumn,childLink) ::=<<
   create procedure <procName>()
     begin
       <drainLeafOneToManyTable(...)>;
     end
>>
createManyToOneStoredProcedure(procName, parentTemp, tableName, tempTableName,idColumn,parentIdColumn,childLink) ::=<<
   create procedure <procName>
     begin
       <truncateTempTable(...)>;
       prepare stmt from "insert into <tempTableName> (<idColumn>) select <idColumn> from <tableName> limit ?,?";
       set @start = theStart;
       set @size = theSize;
       execute stmt using @start,@size;
       <populateManyToOneTempTable(...)>;
       <drainSecondaryTable(...)>;
     end
>>

drainFromStoredProcedureWithStartAndSize(procName,start,size) ::= "call <procName>(<start>,<size>)"

drainFromStoredProcedure(procName) ::= "call <procName>()"

