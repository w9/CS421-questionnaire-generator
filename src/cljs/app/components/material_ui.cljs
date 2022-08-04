(ns app.components.material-ui
  (:refer-clojure :exclude [list])
  (:require
   ["chroma-js" :as chroma]

   ["@mui/material" :as mum]
   ["@mui/material/Badge" :default Badge]
   ["@mui/material/Skeleton" :default Skeleton]
   ["@mui/material/Accordion" :default Accordion]
   ["@mui/material/Autocomplete" :default Autocomplete]
   ["@mui/material/AccordionSummary" :default AccordionSummary]
   ["@mui/material/AccordionDetails" :default AccordionDetails]
   ["@mui/material/Switch" :default Switch]
   ["@mui/material/Chip" :default Chip]
   ["@mui/material/Modal" :default Modal]
   ["@mui/material/Alert" :default Alert]
   ["@mui/material/AlertTitle" :default AlertTitle]
   ["@mui/material/AppBar" :default AppBar]
   ["@mui/material/Avatar" :default Avatar]
   ["@mui/material/Select" :default Select]
   ["@mui/material/Input" :default Input]
   ["@mui/material/InputLabel" :default InputLabel]
   ["@mui/material/InputAdornment" :default InputAdornment]
   ["@mui/material/Menu" :default Menu]
   ["@mui/material/MenuItem" :default MenuItem]
   ["@mui/material/Backdrop" :default Backdrop]
   ["@mui/material/Box" :default Box]
   ["@mui/material/Breadcrumbs" :default Breadcrumbs]
   ["@mui/material/Button" :default Button]
   ["@mui/material/ButtonBase" :default ButtonBase]
   ["@mui/material/ButtonGroup" :default ButtonGroup]
   ["@mui/material/Card" :default Card]
   ["@mui/material/CardActionArea" :default CardActionArea]
   ["@mui/material/CardActions" :default CardActions]
   ["@mui/material/Container" :default Container]
   ["@mui/material/Stack" :default Stack]
   ["@mui/material/CardContent" :default CardContent]
   ["@mui/material/CardHeader" :default CardHeader]
   ["@mui/material/ToggleButtonGroup" :default ToggleButtonGroup]
   ["@mui/material/ToggleButton" :default ToggleButton]
   ["@mui/material/CircularProgress" :default CircularProgress]
   ["@mui/material/Collapse" :default Collapse]
   ["@mui/material/CssBaseline" :default CssBaseline]
   ["@mui/material/Dialog" :default Dialog]
   ["@mui/material/DialogActions" :default DialogActions]
   ["@mui/material/DialogContent" :default DialogContent]
   ["@mui/material/DialogContentText" :default DialogContentText]
   ["@mui/material/DialogTitle" :default DialogTitle]
   ["@mui/material/Divider" :default Divider]
   ["@mui/material/FormControl" :default FormControl]
   ["@mui/material/FormControlLabel" :default FormControlLabel]
   ["@mui/material/FormGroup" :default FormGroup]
   ["@mui/material/FormHelperText" :default FormHelperText]
   ["@mui/material/FormLabel" :default FormLabel]
   ["@mui/material/IconButton" :default IconButton]
   ["@mui/material/Link" :default Link]
   ["@mui/material/List" :default List]
   ["@mui/material/ListItem" :default ListItem]
   ["@mui/material/ListItemIcon" :default ListItemIcon]
   ["@mui/material/ListItemText" :default ListItemText]
   ["@mui/material/ListSubheader" :default ListSubheader]
   ["@mui/material/Paper" :default Paper]
   ["@mui/material/Popover" :default Popover]
   ["@mui/material/Popper" :default Popper]
   ["@mui/material/Portal" :default Portal]
   ["@mui/material/Checkbox" :default Checkbox]
   ["@mui/material/Radio" :default Radio]
   ["@mui/material/RadioGroup" :default RadioGroup]
   ["@mui/material/ScopedCssBaseline" :default ScopedCssBaseline]
   ["@mui/material/Slider" :default Slider]
   ["@mui/material/Tab" :default Tab]
   ["@mui/material/Table" :default Table]
   ["@mui/material/TableBody" :default TableBody]
   ["@mui/material/TableCell" :default TableCell]
   ["@mui/material/TableContainer" :default TableContainer]
   ["@mui/material/TableHead" :default TableHead]
   ["@mui/material/TableRow" :default TableRow]
   ["@mui/material/Tabs" :default Tabs]
   ["@mui/material/TextField" :default TextField]
   ["@mui/material/Typography" :default Typography]

   ["@mui/material/styles" :refer [ThemeProvider createTheme useTheme]]

   ["@mui/icons-material/utils/createSvgIcon" :default createSvgIcon]

   ["@mui/lab/TreeItem" :default TreeItem]
   ["@mui/lab/LoadingButton" :default LoadingButton]
   ["@mui/lab/TreeView" :default TreeView]
   ["@mui/lab/TreeView/TreeViewContext" :default TreeViewContext]

   [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.dom :as dom]
   [taoensso.timbre :as log]
   [goog.object :as gobj]
   [applied-science.js-interop :as j]))

(def ^:deprecated ^{:superseded-by "dialog"} modal (interop/react-factory Modal))
(def switch (interop/react-factory Switch))
(def accordion (interop/react-factory Accordion))
(def autocomplete (interop/react-factory Autocomplete))
(def accordion-summary (interop/react-factory AccordionSummary))
(def accordion-details (interop/react-factory AccordionDetails))
(def alert (interop/react-factory Alert))
(def alert-title (interop/react-factory AlertTitle))
(def container (interop/react-factory Container))
(def stack (interop/react-factory Stack))
(def app-bar (interop/react-factory AppBar))
(def avatar (interop/react-factory Avatar))
(def select (interop/react-factory Select))
(def input (interop/react-factory Input))
(def input-label (interop/react-factory InputLabel))
(def input-adornment (interop/react-factory InputAdornment))
(def menu-item (interop/react-factory MenuItem))
(def menu-list (interop/react-factory mum/MenuList))
(def backdrop (interop/react-factory Backdrop))
(def box (interop/react-factory Box))
(def breadcrumbs (interop/react-factory Breadcrumbs))
(def button (interop/react-factory Button))
(def button-base (interop/react-factory ButtonBase))
(def menu (interop/react-factory Menu))
(def loading-button (interop/react-factory LoadingButton))
(def button-group (interop/react-factory ButtonGroup))
(def c-paper Paper)
(def card (interop/react-factory Card))
(def card-action-area (interop/react-factory CardActionArea))
(def card-actions (interop/react-factory CardActions))
(def card-content (interop/react-factory CardContent))
(def card-header (interop/react-factory CardHeader))
(def circular-progress (interop/react-factory CircularProgress))
(def collapse (interop/react-factory Collapse))
(def css-baseline (interop/react-factory CssBaseline))
(def dialog (interop/react-factory Dialog))
(def dialog-actions (interop/react-factory DialogActions))
(def dialog-content (interop/react-factory DialogContent))
(def dialog-content-text (interop/react-factory DialogContentText))
(def dialog-title (interop/react-factory DialogTitle))
(def divider (interop/react-factory Divider))
(def form-group (interop/react-factory FormGroup))
(def form-control (interop/react-factory FormControl))
(def form-helper-text (interop/react-factory FormHelperText))
(def form-control-label (interop/react-factory FormControlLabel))
(def form-label (interop/react-factory FormLabel))
(def icon-button (interop/react-factory IconButton))
(def link (interop/react-factory Link))
(def list (interop/react-factory List))
(def list-item (interop/react-factory ListItem))
(def list-item-icon (interop/react-factory ListItemIcon))
(def list-item-text (interop/react-factory ListItemText))
(def list-subheader (interop/react-factory ListSubheader))
(def paper (interop/react-factory Paper))
(def popover (interop/react-factory Popover))
(def popper (interop/react-factory Popper))
(def portal (interop/react-factory Portal))
(def checkbox (interop/react-factory Checkbox))
(def chip (interop/react-factory Chip))
(def radio (interop/react-factory Radio))
(def toggle-button-group (interop/react-factory ToggleButtonGroup))
(def toggle-button (interop/react-factory ToggleButton))
(def radio-group (interop/react-factory RadioGroup))
(def scoped-css-baseline (interop/react-factory ScopedCssBaseline))
(def skeleton (interop/react-factory Skeleton))
(def slider (interop/react-factory Slider))
(def t (interop/react-factory Typography))
(def tab (interop/react-factory Tab))
(def table (interop/react-factory Table))
(def table-body (interop/react-factory TableBody))
(def table-cell (interop/react-factory TableCell))
(def table-container (interop/react-factory TableContainer))
(def table-head (interop/react-factory TableHead))
(def table-row (interop/react-factory TableRow))
(def tabs (interop/react-factory Tabs))
(def text-field (interop/react-factory TextField))
(def theme-provider (interop/react-factory ThemeProvider))
(def tree-item (interop/react-factory TreeItem))
(def tree-view (interop/react-factory TreeView))
(def badge (interop/react-factory Badge))
(def tree-view-context TreeViewContext)

(def i-cart-plus (interop/react-factory (createSvgIcon (dom/path {:d "M11,9H13V6H16V4H13V1H11V4H8V6H11M7,18A2,2 0 0,0 5,20A2,2 0 0,0 7,22A2,2 0 0,0 9,20A2,2 0 0,0 7,18M17,18A2,2 0 0,0 15,20A2,2 0 0,0 17,22A2,2 0 0,0 19,20A2,2 0 0,0 17,18M7.17,14.75L7.2,14.63L8.1,13H15.55C16.3,13 16.96,12.59 17.3,11.97L21.16,4.96L19.42,4H19.41L18.31,6L15.55,11H8.53L8.4,10.73L6.16,6L5.21,4L4.27,2H1V4H3L6.6,11.59L5.25,14.04C5.09,14.32 5,14.65 5,15A2,2 0 0,0 7,17H19V15H7.42C7.29,15 7.17,14.89 7.17,14.75Z"}))))
(def i-check (interop/react-factory (createSvgIcon (dom/path {:d "M21,7L9,19L3.5,13.5L4.91,12.09L9,16.17L19.59,5.59L21,7Z"}))))
(def i-help-circle-outline (interop/react-factory (createSvgIcon (dom/path {:d "M11,18H13V16H11V18M12,2A10,10 0 0,0 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12A10,10 0 0,0 12,2M12,20C7.59,20 4,16.41 4,12C4,7.59 7.59,4 12,4C16.41,4 20,7.59 20,12C20,16.41 16.41,20 12,20M12,6A4,4 0 0,0 8,10H10A2,2 0 0,1 12,8A2,2 0 0,1 14,10C14,12 11,11.75 11,15H13C13,12.75 16,12.5 16,10A4,4 0 0,0 12,6Z"}))))
(def i-form-dropdown (interop/react-factory (createSvgIcon (dom/path {:d "M17 5H20L18.5 7L17 5M3 2H21C22.11 2 23 2.9 23 4V8C23 9.11 22.11 10 21 10H16V20C16 21.11 15.11 22 14 22H3C1.9 22 1 21.11 1 20V4C1 2.9 1.9 2 3 2M3 4V8H14V4H3M21 8V4H16V8H21M3 20H14V10H3V20M5 12H12V14H5V12M5 16H12V18H5V16Z"}))))
(def i-refresh (interop/react-factory (createSvgIcon (dom/path {:d "M17.65,6.35C16.2,4.9 14.21,4 12,4A8,8 0 0,0 4,12A8,8 0 0,0 12,20C15.73,20 18.84,17.45 19.73,14H17.65C16.83,16.33 14.61,18 12,18A6,6 0 0,1 6,12A6,6 0 0,1 12,6C13.66,6 15.14,6.69 16.22,7.78L13,11H20V4L17.65,6.35Z"}))))
(def i-select-all (interop/react-factory (createSvgIcon (dom/path {:d "M9,9H15V15H9M7,17H17V7H7M15,5H17V3H15M15,21H17V19H15M19,17H21V15H19M19,9H21V7H19M19,21A2,2 0 0,0 21,19H19M19,13H21V11H19M11,21H13V19H11M9,3H7V5H9M3,17H5V15H3M5,21V19H3A2,2 0 0,0 5,21M19,3V5H21A2,2 0 0,0 19,3M13,3H11V5H13M3,9H5V7H3M7,21H9V19H7M3,13H5V11H3M3,5H5V3A2,2 0 0,0 3,5Z"}))))
(def i-filter-outline (interop/react-factory (createSvgIcon (dom/path {:d "M15,19.88C15.04,20.18 14.94,20.5 14.71,20.71C14.32,21.1 13.69,21.1 13.3,20.71L9.29,16.7C9.06,16.47 8.96,16.16 9,15.87V10.75L4.21,4.62C3.87,4.19 3.95,3.56 4.38,3.22C4.57,3.08 4.78,3 5,3V3H19V3C19.22,3 19.43,3.08 19.62,3.22C20.05,3.56 20.13,4.19 19.79,4.62L15,10.75V19.88M7.04,5L11,10.06V15.58L13,17.58V10.05L16.96,5H7.04Z"}))))
(def i-filter-off-outline (interop/react-factory (createSvgIcon (dom/path {:d "M2.39 1.73L1.11 3L9 10.89V15.87C8.96 16.16 9.06 16.47 9.29 16.7L13.3 20.71C13.69 21.1 14.32 21.1 14.71 20.71C14.94 20.5 15.04 20.18 15 19.88V16.89L20.84 22.73L22.11 21.46L15 14.35V14.34L13 12.35L11 10.34L4.15 3.5L2.39 1.73M6.21 3L8.2 5H16.96L13.11 9.91L15 11.8V10.75L19.79 4.62C20.13 4.19 20.05 3.56 19.62 3.22C19.43 3.08 19.22 3 19 3H6.21M11 12.89L13 14.89V17.58L11 15.58V12.89Z"}))))
(def i-arrow-expand-horizontal (interop/react-factory (createSvgIcon (dom/path {:d "M9,11H15V8L19,12L15,16V13H9V16L5,12L9,8V11M2,20V4H4V20H2M20,20V4H22V20H20Z"}))))
(def i-dots-vertical (interop/react-factory (createSvgIcon (dom/path {:d "M12,16A2,2 0 0,1 14,18A2,2 0 0,1 12,20A2,2 0 0,1 10,18A2,2 0 0,1 12,16M12,10A2,2 0 0,1 14,12A2,2 0 0,1 12,14A2,2 0 0,1 10,12A2,2 0 0,1 12,10M12,4A2,2 0 0,1 14,6A2,2 0 0,1 12,8A2,2 0 0,1 10,6A2,2 0 0,1 12,4Z"}))))
(def i-filter (interop/react-factory (createSvgIcon (dom/path {:d "M14,12V19.88C14.04,20.18 13.94,20.5 13.71,20.71C13.32,21.1 12.69,21.1 12.3,20.71L10.29,18.7C10.06,18.47 9.96,18.16 10,17.87V12H9.97L4.21,4.62C3.87,4.19 3.95,3.56 4.38,3.22C4.57,3.08 4.78,3 5,3V3H19V3C19.22,3 19.43,3.08 19.62,3.22C20.05,3.56 20.13,4.19 19.79,4.62L14.03,12H14Z"}))))
(def i-sort (interop/react-factory (createSvgIcon (dom/path {:d "M18 21L14 17H17V7H14L18 3L22 7H19V17H22M2 19V17H12V19M2 13V11H9V13M2 7V5H6V7H2Z"}))))
(def i-sort-descending (interop/react-factory (createSvgIcon (dom/path {:d "M19 7H22L18 3L14 7H17V21H19M2 17H12V19H2M6 5V7H2V5M2 11H9V13H2V11Z"}))))
(def i-sort-ascending (interop/react-factory (createSvgIcon (dom/path {:d "M19 17H22L18 21L14 17H17V3H19M2 17H12V19H2M6 5V7H2V5M2 11H9V13H2V11Z"}))))
(def i-checkbox-blank-outline (interop/react-factory (createSvgIcon (dom/path {:d "M19,3H5C3.89,3 3,3.89 3,5V19A2,2 0 0,0 5,21H19A2,2 0 0,0 21,19V5C21,3.89 20.1,3 19,3M19,5V19H5V5H19Z"}))))
(def i-select-off (interop/react-factory (createSvgIcon (dom/path {:d "M1,4.27L2.28,3L21,21.72L19.73,23L17,20.27V21H15V19H15.73L5,8.27V9H3V7H3.73L1,4.27M20,3A1,1 0 0,1 21,4V5H19V3H20M15,5V3H17V5H15M11,5V3H13V5H11M7,5V3H9V5H7M11,21V19H13V21H11M7,21V19H9V21H7M4,21A1,1 0 0,1 3,20V19H5V21H4M3,15H5V17H3V15M21,15V17H19V15H21M3,11H5V13H3V11M21,11V13H19V11H21M21,7V9H19V7H21Z"}))))
(def i-table-large (interop/react-factory (createSvgIcon (dom/path {:d "M4,3H20A2,2 0 0,1 22,5V20A2,2 0 0,1 20,22H4A2,2 0 0,1 2,20V5A2,2 0 0,1 4,3M4,7V10H8V7H4M10,7V10H14V7H10M20,10V7H16V10H20M4,12V15H8V12H4M4,20H8V17H4V20M10,12V15H14V12H10M10,20H14V17H10V20M20,20V17H16V20H20M20,12H16V15H20V12Z"}))))
(def i-multiple-sort (interop/react-factory (createSvgIcon (dom/path {:d "M 13 3 L 9 7 L 12 7 L 12 15 L 14 15 L 14 7 L 17 7 L 13 3 z M 2 5 L 2 7 L 6 7 L 6 5 L 2 5 z M 17 9 L 17 17 L 14 17 L 18 21 L 22 17 L 19 17 L 19 9 L 17 9 z M 2 11 L 2 13 L 8 13 L 8 11 L 2 11 z M 2 17 L 2 19 L 10 19 L 10 17 L 2 17 z "}))))
(def i-format-vertical-align-top (interop/react-factory (createSvgIcon (dom/path {:d "M8,11H11V21H13V11H16L12,7L8,11M4,3V5H20V3H4Z"}))))
(def i-upload (interop/react-factory (createSvgIcon (dom/path {:d "M9,16V10H5L12,3L19,10H15V16H9M5,20V18H19V20H5Z"}))))
(def i-update (interop/react-factory (createSvgIcon (dom/path {:d "M21,10.12H14.22L16.96,7.3C14.23,4.6 9.81,4.5 7.08,7.2C4.35,9.91 4.35,14.28 7.08,17C9.81,19.7 14.23,19.7 16.96,17C18.32,15.65 19,14.08 19,12.1H21C21,14.08 20.12,16.65 18.36,18.39C14.85,21.87 9.15,21.87 5.64,18.39C2.14,14.92 2.11,9.28 5.62,5.81C9.13,2.34 14.76,2.34 18.27,5.81L21,3V10.12M12.5,8V12.25L16,14.33L15.28,15.54L11,13V8H12.5Z"}))))

(def use-theme useTheme)

(def shared-theme-config {"typography" {"fontSize" 12}
                          "components" {;; "MuiPaper" {"defaultProps" {"variant" "outlined"}}
                                        "MuiStack" {"defaultProps" {"overflow" "clip"
                                                                    "flex" "1 1 0"}}
                                        "MuiChip" {"variants" [{"props" {"variant" "table-header"}
                                                                "style" {"height" "16px"
                                                                         "overflow" "clip"
                                                                         "& .MuiChip-label" {"padding" "0 2px 0 6px"
                                                                                             "textOverflow" "ellipsis"}
                                                                         "& .MuiChip-deleteIcon" {"fontSize" "12px"
                                                                                                  "margin" "0 1px 0 1px"}}}]}
                                        "MuiButton" {"defaultProps" {"variant" "outlined"}}
                                        "MuiAccordion" {"defaultProps" {"variant" "outlined"
                                                                        "defaultExpanded" true
                                                                        "square" false}}}})

(def light-theme (createTheme (clj->js
                               (merge shared-theme-config))))

(def dark-theme (createTheme (clj->js
                              (merge shared-theme-config
                                     {"palette" {"mode" "dark"}}))))
