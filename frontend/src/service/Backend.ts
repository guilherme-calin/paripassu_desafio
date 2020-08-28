import Numbering from "../model/Numbering";

export default class Backend{
    static backendUrl :string = "/paripassu/api";
    constructor(){}

    static getLastServed(lastServedId? :number) :Promise<Numbering[]>{
        return new Promise((resolve, reject) => {
            let endpoint :string = "/user/getLastServed";

            if(lastServedId){
                endpoint += `?lastServedId=${lastServedId};`
            }

            fetch(this.backendUrl+endpoint).then(response => {
                return response.json();
            }).then(jsonResponse => {
                if(jsonResponse.success){
                    let numberings :Numbering[] = [];

                    let list :any = jsonResponse.result.list;

                    for(let item of list){
                        numberings.push(new Numbering(item.id, item.numberingCode, item.numberingType, item.dateTimeRequest, item.dateTimeServed));
                    }

                    resolve(numberings);
                }else{
                    throw new Error("Não houve sucesso na requisição!");
                }

                }).catch(err => {
                    reject(err);
                });
        })
    }

    static generateNumbering(numberingType :string) :Promise<Numbering>{
        return new Promise((resolve, reject) => {
            let endpoint :string = "/user/generate";
            let body :any = {
                numberingType
            }
            fetch(this.backendUrl+endpoint, {method : "POST", body : JSON.stringify(body), headers : {"Content-Type" : "application/json"}}).then(response => {
                return response.json();
            }).then(jsonResponse => {
                if(jsonResponse.success){
                    let newNumbering :any = jsonResponse.result.newNumbering;

                    let numbering :Numbering = new Numbering(newNumbering.id, newNumbering.numberingCode, newNumbering.numberingType, newNumbering.dateTimeRequest);

                    console.log(numbering);
                    resolve(numbering);
                }else{
                    throw new Error("Não houve sucesso na requisição!");
                }
            }).catch(err => {
                reject(err);
            });
        })
    }

    static serveNext() : Promise<Numbering>{
        return new Promise((resolve, reject) => {
            let endpoint :string = "/manager/serveNext";
            fetch(this.backendUrl+endpoint, {method : "PUT"}).then(response => {
                return response.json();
            }).then(jsonResponse => {
                if(jsonResponse.success && jsonResponse.result.numbering){
                    let num :any = jsonResponse.result.numbering;
                    let numbering :Numbering = new Numbering(num.id, num.numberingCode, num.numberingType, num.dateTimeRequest, num.dateTimeServed);
                    resolve(numbering);
                }else{
                    reject(jsonResponse.result.message);
                }
            }).catch(err => {
                console.log(err);
                alert(`Não foi possível chamar a próxima senha. O motivo informado é: ${err.message}`);
                reject(null);
            });
        })
    }

    static resetNumbering() : Promise<boolean>{
        return new Promise((resolve, reject) => {
            let endpoint :string = "/manager/resetNumbering";
            fetch(this.backendUrl+endpoint, {method : "PUT"}).then(response => {
                return response.json();
            }).then(jsonResponse => {
                resolve (jsonResponse.success);
            }).catch(err => {
                reject(false);
            });
        })
    }
}