import React from "react";
import Link from "react-router-dom/es/Link";

export default class EventRouteManager extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <ul className="pt-breadcrumbs">
                    <li><Link className="pt-breadcrumbs-collapsed" to="/"/></li>
                    <li><span className="pt-breadcrumb pt-breadcrumb-current">Routes</span></li>
                </ul>
            </div>
        )
    }
}