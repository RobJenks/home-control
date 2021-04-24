import React from 'react';
import * as Events from 'types/events';

type EventDataStreamProps = {
    data: Events.Data[]
}

class EventDataStream extends React.Component<EventDataStreamProps, {}> {

    render() {
        return (
            <div className="App">
              <div>
                <table className="eventTable">
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>Event</th>
                    </tr>
                  </thead>
                  
                  <tbody>
                    {
                        this.props.data.map((ev, i) => {
                            return (
                                <tr key={ev.offset}>
                                <td>{ev.offset}</td>
                                <td>{JSON.stringify(ev)}</td>
                                </tr>
                            )
                        })
                    }
                  </tbody>
                </table>
              </div>
              
            </div>
          );
    }
}

export default EventDataStream;
