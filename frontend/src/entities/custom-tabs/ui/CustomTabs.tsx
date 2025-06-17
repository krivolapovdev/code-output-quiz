import { Tabs } from 'antd';

const items = [
  {
    key: 'beginner',
    label: 'Beginner',
    style: { padding: '0' }
  },
  {
    key: 'intermediate',
    label: 'Intermediate'
  },
  {
    key: 'advanced',
    label: 'Advanced'
  }
];

export const CustomTabs = () => {
  const onChange = (key: string) => {
    console.log(key);
  };

  return (
    <div className='flex justify-end'>
      <Tabs type='card' onChange={onChange} items={items} tabBarStyle={{ margin: 0, borderRadius: 0 }} />
    </div>
  );
};
