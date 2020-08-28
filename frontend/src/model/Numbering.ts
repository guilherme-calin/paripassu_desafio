export default class Numbering{
    private id :number;
    private numberingCode :string;
    private numberingType :string;
    private epochDateTimeRequest :number | null;
    private epochDateTimeServed :number | null;

    constructor(id :number, code :string, type :string, epochDateTimeRequest? :number, epochDateTimeServed? :number){
        this.id = id;
        this.numberingCode = code;
        this.numberingType = type;
        this.epochDateTimeRequest = epochDateTimeRequest || null;
        this.epochDateTimeServed = epochDateTimeServed || null;
    }

    public getId(): number {
        return this.id;
    }

    public getFullNumberingCode() :string {
        return this.numberingType + this.numberingCode;
    }

    public getNumberingType() :string {
        return this.numberingType;
    }

    public getEpochDateTimeRequest() :number{
        return this.epochDateTimeRequest ? this.epochDateTimeRequest : 0;
    }

    public getEpochDateTimeServed() :number{
        return this.epochDateTimeServed ? this.epochDateTimeServed : 0;
    }

    public getRequestTime() :string{
        if(this.epochDateTimeRequest){
            let requestDate :Date = new Date(this.epochDateTimeRequest);
            let time = `${requestDate.getHours().toString().padStart(2, "0")}h${requestDate.getMinutes().toString().padStart(2, "0")}`;

            return time;
        }else{
            return "--h--"
        }
    }

    public getServedTime() :string{
        if(this.epochDateTimeServed){
            let servedDate :Date = new Date(this.epochDateTimeServed);

            let time = `${servedDate.getHours().toString().padStart(2, "0")}h${servedDate.getMinutes().toString().padStart(2, "0")}`;

            return time;
        }else{
            return "--h--"
        }
    }
}