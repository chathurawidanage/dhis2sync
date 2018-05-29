import React from "react";
import {Spinner} from "@blueprintjs/core/lib/cjs/components/spinner/spinner";

export default class LoadingComponent extends React.Component {

    render() {
        return (
            <div>
                <div className="pt-fill text-center">
                    {this.props.loading && <Spinner/>}
                </div>
                {!this.props.loading && this.props.children}
            </div>
        );
    }
}