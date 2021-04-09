import './App.css'
import React, { useState, useEffect } from 'react';
import axios from "axios"
import { QueryClient, QueryClientProvider, useQuery, useQueryClient } from "react-query";

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <DoThings />
    </QueryClientProvider>
  )
}

function DoThings() {
  const [pollIntervalMs, setPollIntervalMs] = React.useState(2000);
  const targetUrl = process.env.REACT_APP_LOCAL_SERVICE_URL + "/updates?count=40"

  const { isLoading, error, data, isFetching } = 
    useQuery("testDataQuery", 
    async () => axios.get(targetUrl),
    {
      refetchInterval: pollIntervalMs
    }
  );

  if (error) {
    var err = "Query error: " + error.message;
    console.error(err);
    return err;
  }

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
            {cleanData(data).map((ev, i) => {
              return (
                <tr key={ev.offset}>
                  <td>{ev.offset}</td>
                  <td>{JSON.stringify(ev)}</td>
                </tr>
              )
            })}
          </tbody>
        </table>
      </div>
      
    </div>
  );
}

function cleanData(queryResponse) {
  if (queryResponse) {
    if (queryResponse.data) {
      return queryResponse.data.data;
    }
  }

  return [];
}


export default App;

