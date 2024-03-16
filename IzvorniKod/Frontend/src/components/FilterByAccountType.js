import React from "react";



let filterByAccountType = (props) => {

    function onFilterValueChanged(event) {
        props.filterValueSelected(event.target.value)
    }

    return(<div>
        <select className="ui selection dropdown" onChange={onFilterValueChanged}>
            <option value="all">All</option>
            <option value="participant">Participant</option>
            <option value="operative">Operative admin</option>
        </select>
    </div>);
}

export default filterByAccountType;