import React, { useState } from "react";


function Locate(props) {
    const[state, setState] = React.useState({
        departureFrom: "",
        arrivalTo: "",
        departDate: "",
        returnDate: "",
        numberOfAdults: 1,
        currency: "EUR"
    })

    const[results, setResults] = React.useState(
        []
    )

    const[error, setError] = React.useState(
        undefined
    )

    const [loading, setLoading] = React.useState(
        false
    )
    const submit = (e) => {
        setResults([]);
        setError(undefined)
        setLoading(true)
        e.preventDefault();
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(state)
        };
        fetch(
            "http://localhost:8080/search", requestOptions
        )
            .then((response) => response.json())
            .then((json) => {
                setResults(json)
                setLoading(false)
            }).catch(err => {
                setError(err)
                setLoading(false)
        })


    }

    function handleChange(evt) {
        const value = evt.target.value;
        setState({
            ...state,
            [evt.target.name]: value
        });

    }

    return (
        <>
        <form onSubmit={submit}>
            <div className={'formInput'}>
            <label>
                Departure from
            </label>
            <input
                type="text"
                name="departureFrom"
                value={state.departureFrom}
                onChange={handleChange}
            />
            </div>
            <div className={'formInput'}>
            <label>
                Arrival destination
            </label>
            <input
                type="text"
                name="arrivalTo"
                value={state.arrivalTo}
                onChange={handleChange}
            />
            </div>
            <div className={'formInput'}>
            <label>
                Depart date
            </label>
            <input
                type="date"
                name="departDate"
                value={state.departDate}
                onChange={handleChange}
            />
            </div>
            <div className={'formInput'}>
            <label>
                Return date
            </label>
            <input
                type="date"
                name="returnDate"
                value={state.returnDate}
                onChange={handleChange}
            />
            </div>
            <div className={'formInput'}>
            <label>
                Number of travellers
            </label>
            <input
                type="text"
                name="numberOfAdults"
                value={state.numberOfAdults}
                onChange={handleChange}
            />
            </div>
            <div className={'formInput'}>
            <label>
                Currency
            </label>
            <select name="currency" onChange={handleChange} value={state.currency}>
                <option value="EUR">EUR</option>
                <option value="USD">USD</option>
                <option value="HRK">HRK</option>
            </select>
            </div>
            <input className={'submitButton'} type="submit" value="Submit"/>
                </form>

            <div className={'results'}>
            {error && <h3>Dogodila se pogreška!</h3>}
            {loading && <h3>Učitavanje...</h3>}
            {results && results.length > 0 && (
                <table>
                    <thead>
                    <tr>
                        <th>Polazni aerodrom </th>
                        <th>Odredišni aerodrom </th>
                        <th>Datum polaska </th>
                        {results[0].returnDate && <th>Datum povratka </th>}
                        <th>Broj presjedanja u odlaznom letu</th>
                        {results[0].returnDate && <th>Broj presjedanja u povratnom letu</th>}
                        <th>Broj slobodnih mjesta </th>
                        <th>Valuta </th>
                        <th>Cijena </th>
                    </tr>
                    </thead>
                    <tbody>
                    {results.map((r,index) => <tr key={index}>
                        <td>{r.departureFrom}</td>
                        <td>{r.arrivalTo}</td>
                        <td>{r.departDate}</td>
                        {r.returnDate && <td>{r.returnDate}</td>}
                        <td>{r.numberOfTransfersOnDeparture}</td>
                        {r.returnDate && <td>{r.numberOfTransfersOnArrival}</td>}
                        <td>{r.numberOfAdults}</td>
                        <td>{r.currency}</td>
                        <td>{r.price}</td>
                    </tr>)}
                    </tbody>
                </table>
            )}
            </div>
        </>

    );

};

export default Locate;
