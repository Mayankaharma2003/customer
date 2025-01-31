import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityCondition
import org.moqui.entity.EntityFind
import java.sql.Timestamp;


ExecutionContext excutionContext = context.ec
EntityFind entityFind= excutionContext.entity.find("oms.person.FindCustomerView").distinct(true)
entityFind.selectFields(Arrays.asList("contactMechPurposeId","infostring"))
if(infoString){
    entityFind.condition(
            excutionContext.entity.conditionFactory.makeCondition("infoString",EntityCondition.LIKE,(leadingWildCard?"%":"") + infoString + "%").ignoreCase()
    )
}
EntityList partyList = entityFind.list()
for (EntityValue record in partyList){
    if(record.contactMechPurposeId=="EmailPrimary"){
        return;
    }
}
def party = excutionContext.entity.makeValue("oms.party.Party")
party.set("partyTypeEnumId","PERSON")
party.setSequencedIdPrimary().create()
partyId=party.partyId


def person = excutionContext.entity.makeValue("oms.party.Person")
person.set("partyId",partyId)
person.set("firstName",context.firstName)
person.set("lastName",context.lastName)
person.set("birthDate",context.birthDate)
person.create()


def contact= excutionContext.entity.makeValue("oms.contact.ContactMech")
contact.set("contactMechTypeEnumId","EMAIL_ADDRESS")
contact.set("infoString",context.infoString)
contact.setSequencedIdPrimary().create()
contactMechId=contact.contactMechId


def partyContact= excutionContext.entity.makeValue("oms.contact.PartyContactMech")
partyContact.set("partyId",partyId)
partyContact.set("contactMechId",contactMechId)
partyContact.set("contactMechPurposeId","EmailPrimary")
partyContact.set("fromDate",new Timestamp(new Date().getTime()))
partyContact.create()