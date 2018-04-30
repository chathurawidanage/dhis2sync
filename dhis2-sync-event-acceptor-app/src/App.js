import React, {Component} from 'react';
import "./style/app.css";
import {
    Navbar,
    NavbarDivider,
    NavbarGroup,
    Button,
    NavbarHeading,
    Alignment,
    Menu,
    MenuItem,
    MenuDivider
} from "@blueprintjs/core";
import {SyncDataElementsComponent} from "./components/SyncDataElements/SyncDataElementsComponent";
import {Link, Route, Switch} from "react-router-dom";
import DHIS2InstancesManager from "./components/DHIS2Instances/DHIS2InstancesManager";
import DHIS2Instances from "./components/DHIS2Instances/DHIS2Instance";
import {Toaster, Position} from "@blueprintjs/core";

class App extends Component {
    render() {
        return (
            <div className="app">
                <Navbar>
                    <NavbarGroup align={Alignment.LEFT}>
                        <NavbarHeading>DHIS2 - Sync</NavbarHeading>
                        <NavbarDivider/>
                        <Button className="pt-minimal" icon="home" text="Home"/>
                        <Button className="pt-minimal" icon="document" text="Files"/>
                    </NavbarGroup>
                </Navbar>
                <div className="app-content">
                    <Menu className="pt-elevation-1 app-content-sidebar">
                        <Link to="/dataElements">
                            <MenuItem icon="layers" text="Data Elements"/>
                        </Link>
                        <Link to="/dhis2Instances">
                            <MenuItem icon="database" onClick={this.handleClick} text="DHIS2 Instances"/>
                        </Link>
                        <MenuItem icon="flows" onClick={this.handleClick} text="Event Routes"/>
                        <MenuDivider/>
                        <MenuItem text="Settings..." icon="cog"/>
                    </Menu>
                    <div className="app-content-ui">
                        <Switch>
                            <Route path="/dataElements" component={SyncDataElementsComponent}/>
                            <Route path="/dhis2Instances/:instanceId" component={DHIS2Instances}/>
                            <Route path="/dhis2Instances" component={DHIS2InstancesManager}/>
                        </Switch>
                    </div>
                </div>
                <div className="app-footer">
                    &copy; HISP Sri Lanka
                </div>
            </div>
        );
    }
}

export const AppToaster = Toaster.create({
    className: "recipe-toaster",
    position: Position.BOTTOM_RIGHT,
});

export default App;
