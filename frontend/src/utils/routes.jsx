// routes.jsx
import Home from "../pages/Home";
import Friends from "../pages/Friends";
import Video from "../pages/Video";
import Market from "../pages/Market";
import Game from "../pages/Game";
import Login from "../pages/QDTLogin";
import Register from "../pages/QDTRegister";

const routes = [
  { path: "/", element: <Home />, protected: true },
  { path: "/home", element: <Home />, protected: true },
  { path: "/friends", element: <Friends />, protected: true },
  { path: "/videos", element: <Video />, protected: true },
  { path: "/marketplace", element: <Market />, protected: true },
  { path: "/games", element: <Game />, protected: true },
  { path: "/login", element: <Login />},
  { path: "/register", element: <Register />},
];

export default routes;
