import { BrowserRouter, Routes, Route } from "react-router-dom";
import routes from "./utils/routes";

import NavBar from "./components/NavBar";

const App = () => {
  return ( 
    <BrowserRouter>
      <NavBar />

      <Routes>
        {routes.map((r, index) => (
          <Route key={index} path={r.path} element={r.element} />
        ))}
      </Routes>

    </BrowserRouter>
  );
}
 
export default App;