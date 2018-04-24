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
                        <MenuItem icon="new-text-box" onClick={this.handleClick} text="New text box"/>
                        <MenuItem icon="new-object" onClick={this.handleClick} text="New object"/>
                        <MenuItem icon="new-link" onClick={this.handleClick} text="New link"/>
                        <MenuDivider/>
                        <MenuItem text="Settings..." icon="cog"/>
                    </Menu>
                    <div className="app-content-ui">
                        <SyncDataElementsComponent/>
                    </div>
                </div>
            </div>
        );
    }
}

export default App;
