# UIHop

UIHop is plugin for Intellij IDEs that highlights clickable/interactable components on the IDE, each with their own unique labels.

![UIHop](https://github.com/user-attachments/assets/4c27182f-8877-4c3b-a505-530a722f354d)
![UIHop2](https://github.com/user-attachments/assets/ece0f69f-42bb-4a0b-9593-f4cd6b6c99c2)

[Alternative Link to Demos (Imgur)](https://imgur.com/a/tG65Ea0)

## Usage
The highlighting can be triggered by simply activating the UIHop action (default keymap is `shift G`), and pressing again to remove highlights.

Alternatively:
This can be edited by going to `File | Settings | Keymap | Plugins -> UIHop`

Or if using Ideavim:
You can map `UIHop` in `~/.ideavimrc`, for example by adding:
```
map <leader>f <Action>(UIHop)
```

## Installation

- Download the [latest release](https://github.com/SushantPulavarthi/UIHop/releases) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Clone the repository, and run the gradle task `buildPlugin`. This will build the distribution to `build/distribution`, which you can use to install the plugin to Intellij
```
git clone https://github.com/SushantPulavarthi/UIHop.git
cd UIHop
./gradlew buildPlugin
```

## TODO

- Find better way to assign labels, with a better algorithm and potentially a way that makes it more intuitive and easier to type.
- Be able to activate the clickable by pressing the relevant keycombination

