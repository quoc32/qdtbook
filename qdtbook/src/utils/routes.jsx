// routes.jsx
import Home from "../pages/Home";
import Friends from "../pages/Friends";
import Video from "../pages/Video";
import Market from "../pages/Market";
import Game from "../pages/Game";
import Login from "../pages/QDTLogin";
import Register from "../pages/QDTRegister";

const routes = [
  { path: "/", element: <Home /> },
  { path: "/home", element: <Home /> },
  { path: "/friends", element: <Friends /> },
  { path: "/videos", element: <Video /> },
  { path: "/marketplace", element: <Market /> },
  { path: "/games", element: <Game /> },
  { path: "/login", element: <Login />},
  { path: "/register", element: <Register />},
];

export default routes;
