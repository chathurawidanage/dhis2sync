import React from "react";
import {Spinner, Button} from "@blueprintjs/core";
import "./ButtonWithSpinner.css";

export default class ButtonWithSpinner extends React.Component {

    render() {
        return (
            <div className="button-with-spinner">
                <Button onClick={this.props.onClick} disabled={this.props.disabled} text={this.props.text}>
                    {this.props.loading && <Spinner small={true} style={{width: 15}}/>}
                </Button>
            </div>
        );
    }
}