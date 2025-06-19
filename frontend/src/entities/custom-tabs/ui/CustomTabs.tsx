import { Tabs } from "antd";
import { useSupportedDifficultyLevels } from "@/entities/quizzes";

export const CustomTabs = () => {
  const { levels, loading } = useSupportedDifficultyLevels();

  const onChange = (key: string) => {
    console.log(key);
  };

  if (loading) {
    return <div>Loading tabs...</div>;
  }

  const items = levels.map(lvl => ({
    key: lvl,
    label: lvl.charAt(0).toUpperCase() + lvl.slice(1).toLowerCase(),
    style: { padding: "0" }
  }));

  return (
    <div className="flex justify-end">
      <Tabs
        type="card"
        onChange={onChange}
        items={items}
        tabBarStyle={{ margin: 0, borderRadius: 0 }}
      />
    </div>
  );
};
