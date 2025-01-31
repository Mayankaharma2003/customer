import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityCondition
import org.moqui.entity.EntityFind
import java.sql.Timestamp;


ExecutionContext excutionContext = context.ec
EntityFind entityFind= excutionContext.entity.find("oms.person.FindCustomerView").distinct(true)
//entityFind.selectFields(Arrays.asList("partyId","contactMechId"))
entityFind.selectField("partyId")
if(infoString){
    entityFind.condition(
            excutionContext.entity.conditionFactory.makeCondition(excutionContext.entity.conditionFactory.makeCondition("contactMechPurposeId",EntityCondition.LIKE,"EmailPrimary").ignoreCase(), EntityCondition.AND, excutionContext.entity.conditionFactory.makeCondition("infoString",EntityCondition.LIKE,(leadingWildCard?"%":"") + infoString + "%").ignoreCase()))
}
entityFind.selectField("partyId")
EntityValue party=entityFind.one()
isPartyId = party.partyId


EntityFind find= excutionContext.entity.find("oms.person.FindCustomerView").distinct(true)
find.selectField("contactMechId")
if(isPartyId){
    find.condition(
            excutionContext.entity.conditionFactory.makeCondition(excutionContext.entity.conditionFactory.makeCondition("partyId",EntityCondition.LIKE,(leadingWildCard?"%":"") + isPartyId + "%").ignoreCase(),EntityCondition.AND, excutionContext.entity.conditionFactory.makeCondition("contactMechPurposeId",EntityCondition.LIKE,(leadingWildCard?"%":"") + context.contactMechPurposeId + "%"))
    )
}else{
    return
}
EntityValue entityVal = find.one()
if(entityVal){
    isContactMechId = entityVal.contactMechId
    EntityValue partyContact= excutionContext.entity.find("oms.contact.PartyContactMech").condition('contactMechId', isContactMechId).condition('partyId', isPartyId).one()
    partyContact.set("thruDate", new Timestamp(new Date().getTime()))
    partyContact.update()
}
def contact= excutionContext.entity.makeValue("oms.contact.ContactMech")
contact.set("contactMechTypeEnumId", context.contactMechTypeEnumId)
contact.set("infoString",context.secondInfoString)
contact.setSequencedIdPrimary().create()
contactMech = contact.contactMechId

if(context.contactMechTypeEnumId=="POSTAL_ADDRESS"){
    def postalAddressEntity = excutionContext.entity.makeValue('PostalAddress')
    postalAddressEntity.set('contactMechId', contactMech)
    postalAddressEntity.set('tName', context.tName)
    postalAddressEntity.set('attnName', context.attnName)
    postalAddressEntity.set('address1', context.address1)
    postalAddressEntity.set('address2', context.address2)
    postalAddressEntity.set('city', context.city)
    postalAddressEntity.set('postalAddress', context.postalAddress)
    postalAddressEntity.create()
}
else if(context.contactMechTypeEnumId=="TELECOM_ADDRESS"){
    def telecomNumberEntity = excutionContext.entity.makeValue('TelecomNumber')
    telecomNumberEntity.set('contactMechId', contactMech)
    telecomNumberEntity.set('countryCode', context.countryCode)
    telecomNumberEntity.set('areaCode', context.areaCode)
    telecomNumberEntity.set('contactNumber', context.contactNumber)
    telecomNumberEntity.create()
}

def partyContact= excutionContext.entity.makeValue("oms.contact.PartyContactMech")
partyContact.set("partyId",context.isPartyId)
partyContact.set("contactMechId",contactMech)
partyContact.set("contactMechPurposeId",context.contactMechPurposeId)
partyContact.set("fromDate",new Timestamp(new Date().getTime()))
partyContact.create()

//Map<String, Object> map = new HashMap<String, Object>()
//map = excutionContext.service.sync().name("PartyServices.find").parameters("isEmailPrimary":context.infoString).call()
//allParty=map.get(partyList)
//for(recored in allParty){
//    isPartyId = recored.partyId
//}
//if(infoString){
//    entityFind.condition(
//            excutionContext.entity.conditionFactory.makeCondition("infoString",EntityCondition.LIKE,(leadingWildCard?"%":"") + infoString + "%").ignoreCase()
//    )
//}