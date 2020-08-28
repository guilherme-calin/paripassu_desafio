import React, {Component, ReactNode} from "react";
import './TopBottomNumbering.css';
import Numbering from "../../model/Numbering";

type Props = {
    text :string,
    timeType :string,
    numbering :Numbering
}
type State = {}
export default class TopBottomNumbering extends Component<Props, State>{
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
            <div className="TopBottomNumbering">
                    <div className={`upper-container ${typeClassName}`}>
                        <span>{this.props.text}</span>
                    </div>


                    <div className="code-container">
                        <span>{this.props.numbering.getFullNumberingCode()}</span>
                    </div>


                    <div className={`time-container ${typeClassName}`}>
                        <span>{this.props.timeType === "served" ? this.props.numbering.getServedTime() :
                                    this.props.numbering.getRequestTime()}</span>
                    </div>
            </div>
        )
    }
}
