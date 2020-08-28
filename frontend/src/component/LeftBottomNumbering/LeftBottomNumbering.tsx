import React, {Component, ReactNode} from "react";
import './LeftBottomNumbering.css';
import Numbering from "../../model/Numbering";

type Props = {
    identifier :string,
    timeType :string,
    numbering :Numbering
}
export default class LeftBottomNumbering extends Component<Props>{
    public state :Object = {};

    constructor(props :Props){
        super(props);
    }

    public render() :ReactNode{
        let type = this.props.numbering.getNumberingType();
        let typeClassName :string;

        if(type === "P"){
            typeClassName = "priority";
        }else{
            typeClassName = "normal";
        }

        return(
            <div className="LeftBottomNumbering">
                    <div className={`order-container ${typeClassName}`}>
                        <span>{this.props.identifier}</span>
                    </div>
                    <div className="info-container">
                        <div className="code-container">
                            <span>{this.props.numbering.getFullNumberingCode()}</span>
                        </div>
                        <div className={`time-container ${typeClassName}`}>
                            <span>{this.props.timeType === "served" ? this.props.numbering.getServedTime() :
                                this.props.numbering.getRequestTime()}</span>
                        </div>
                    </div>
            </div>
        )
    }
}
