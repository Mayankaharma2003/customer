import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityCondition
import org.moqui.entity.EntityFind
import org.moqui.entity.EntityList
import org.moqui.entity.EntityValue

ExecutionContext excutionContext = context.ec

EntityFind entityFind =excutionContext.entity.find("oms.person.FindCustomerView").distinct(true)
entityFind.selectField("partyId")

if(partyId){
    entityFind.condition( excutionContext.entity.conditionFactory.makeCondition("partyId", EntityCondition.LIKE, ( leadingWildCard ? "%" : "" ) + partyId + "%").ignoreCase())
}
if(firstName){
    entityFind.condition( excutionContext.entity.conditionFactory.makeCondition("firstName", EntityCondition.LIKE, (leadingWildCard?"%":"") + firstName + "%").ignoreCase())
}
if(lastName){
    entityFind.condition(
            excutionContext.entity.conditionFactory.makeCondition(
                    "lastName",EntityCondition.LIKE,(leadingWildCard?"%":"") + lastName + "%").ignoreCase()
            )
}
if(infoString){
    entityFind.condition(
            excutionContext.entity.conditionFactory.makeCondition(
                    "infoString",EntityCondition.LIKE,(leadingWildCard?"%":"") + infoString + "%").ignoreCase()

    )
}
if(contactNumber){
    entityFind.condition(
            excutionContext.entity.conditionFactory.makeCondition(
                    "contactNumber",EntityCondition.LIKE,(leadingWildCard?"%":"") + contactNumber + "%").ignoreCase()
    )
}
if(tName){
    entityFind.condition(
            excutionContext.entity.conditionFactory.makeCondition(
                    "tName",EntityCondition.LIKE,(leadingWildCard?"%":"") + tName + "%").ignoreCase()
    )
}
if(attnName){
    entityFind.condition(
            excutionContext.entity.conditionFactory.makeCondition(
                    "attnName",EntityCondition.LIKE,(leadingWildCard?"%":"") + attnName + "%").ignoreCase()
    )
}
if(address1){
    entityFind.condition(
            excutionContext.entity.conditionFactory.makeCondition("address1",EntityCondition.LIKE,(leadingWildCard?"%":"") + address1 + "%").ignoreCase())
}
if(address2){
    entityFind.condition(
            excutionContext.entity.conditionFactory.makeCondition("address2",EntityCondition.LIKE,(leadingWildCard?"%":"") + address2 + "%").ignoreCase())
}
if(city){
    entityFind.condition(
            excutionContext.entity.conditionFactory.makeCondition("city",EntityCondition.LIKE,(leadingWildCard?"%":"") + city + "%").ignoreCase())
}
if(contactNumber){
    entityFind.condition(
            excutionContext.entity.conditionFactory.makeCondition("contactNumber",EntityCondition.LIKE,(leadingWildCard?"%":"") + contactNumber + "%").ignoreCase())
}
if(postalAddress){
    entityFind.condition(
            excutionContext.entity.conditionFactory.makeCondition("postalAddress",EntityCondition.LIKE,(leadingWildCard?"%":"") + contactNumber + "%").ignoreCase())
}
if(isEmailPrimary){
    entityFind.condition(
            excutionContext.entity.conditionFactory.makeCondition(excutionContext.entity.conditionFactory.makeCondition("contactMechPurposeId",EntityCondition.LIKE,"EmailPrimary").ignoreCase(), EntityCondition.AND, excutionContext.entity.conditionFactory.makeCondition("infoString",EntityCondition.LIKE,(leadingWildCard?"%":"") + isEmailPrimary + "%").ignoreCase()))
}

entityFind.orderBy("firstName,lastName")




partyIdList = []
EntityList partyList = entityFind.list()
for (EntityValue record in partyList){
    partyIdList.add(record.partyId)
}

partyIdListCount = entityFind.count()
if(partyIdListPageSize==null){
    partyIdListPageSize=1
}
if(partyIdListPageIndex ==null){
    partyIdListPageIndex=1
}

//partyIdListPageIndex = entityFind.pageIndex
//partyIdListPageIndex =partyIdListPageIndex+1
partyIdListPageMaxIndex = ((BigDecimal) (partyIdListCount - 1)).divide(partyIdListPageSize) as int
partyIdListPageRangeLow = partyIdListPageIndex * partyIdListPageSize + 1
partyIdListPageRangeHigh = (partyIdListPageIndex * partyIdListPageSize) + partyIdListPageSize
if (partyIdListPageRangeHigh > partyIdListCount) partyIdListPageRangeHigh = partyIdListCount